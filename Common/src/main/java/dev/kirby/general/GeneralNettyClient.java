package dev.kirby.general;

import dev.kirby.netty.Packet;
import dev.kirby.netty.event.EventRegistry;
import dev.kirby.netty.event.PacketSubscriber;
import dev.kirby.netty.handler.PacketChannelInboundHandler;
import dev.kirby.netty.handler.PacketDecoder;
import dev.kirby.netty.handler.PacketEncoder;
import dev.kirby.netty.registry.IPacketRegistry;
import dev.kirby.packet.empty.ConnectPacket;
import dev.kirby.packet.registry.SendPacket;
import dev.kirby.service.ServiceRegistry;
import dev.kirby.utils.KirbyLogger;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import lombok.Setter;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Getter
public class GeneralNettyClient extends ChannelInitializer<Channel> {

    private final Bootstrap bootstrap;
    private final IPacketRegistry packetRegistry;
    private final EventRegistry eventRegistry = new EventRegistry();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    private final PacketSender packetSender;

    private final KirbyLogger logger;

    private final Runnable shutdownHook;

    @Setter
    private Consumer<ChannelHandlerContext> channelActiveAction;

    private Channel channel;

    private boolean connected;

    public GeneralNettyClient(IPacketRegistry packetRegistry, Runnable shutdownHook, String loggerName, ServiceRegistry serviceRegistry) {
        this.packetRegistry = packetRegistry;
        this.shutdownHook = shutdownHook;
        this.logger = new KirbyLogger(loggerName);
        this.bootstrap = new Bootstrap()
                .option(ChannelOption.AUTO_READ, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .group(workerGroup)
                .handler(this)
                .channel(NioSocketChannel.class);

        packetSender = new PacketSender(serviceRegistry);
        eventRegistry.registerEvents(packetSender);
    }

    private String host;
    private int port;

    public void connect(String host, int port) {
        this.host = host;
        this.port = port;
        try {
            this.bootstrap.connect(new InetSocketAddress(host, port)).sync().addListener((ChannelFutureListener) future1 -> {
                if (future1.isSuccess()) {
                    logger.info("Connected!");
                    this.connected = true;
                    return;
                }
                logger.warn("Connect failed!");
                shutdown();
            });
        } catch (InterruptedException e) {
            logger.error("Connect failed!", e);
            shutdown();
        }

    }

    @Override
    protected void initChannel(Channel channel) {
        (this.channel = channel).pipeline().addLast(new PacketDecoder(packetRegistry), new PacketEncoder(packetRegistry),
                new PacketChannelInboundHandler(eventRegistry) {
                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        super.channelActive(ctx);
                        if (channelActiveAction != null) channelActiveAction.accept(ctx);
                    }

                    @Override
                    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                        super.channelInactive(ctx);
                        logger.warn("Disconnected from server!");
                        if (connected) {
                            connected = false;
                            attemptReconnect();
                        }
                    }
                }
        );
    }

    private final ScheduledExecutorService reconnectExecutor = Executors.newSingleThreadScheduledExecutor();

    private void attemptReconnect() {
        logger.info("Attempting to reconnect in " + 15 + " seconds...");
        reconnectExecutor.schedule(() -> {
            if (!connected) connect(host, port);
        }, 15, TimeUnit.SECONDS);
    }

    public void shutdown() {
        shutdownHook.run();
        packetSender.service.destroy();
        this.connected = false;
        try {
            workerGroup.shutdownGracefully().sync();
        } catch (InterruptedException e) {
            logger.error("Failed to shutdown", e);
        } finally {
            workerGroup.shutdownGracefully(0, 0, TimeUnit.SECONDS).syncUninterruptibly();
        }
    }

    @Setter
    @Getter
    public static class PacketSender {

        private final ServiceRegistry service;

        public PacketSender(ServiceRegistry service) {
            this.service = service;
        }

        private ChannelHandlerContext ctx;

        public void sendPacket(final Packet packet) {
            if (ctx == null) return;
            System.out.println("sending packet: " + packet.getPacketName());
            ctx.writeAndFlush(packet);
        }

        @PacketSubscriber
        public void onConnect(final ConnectPacket packet, final ChannelHandlerContext ctx) {
            this.ctx = ctx;
            install(SendPacket.class, this::sendPacket);
        }

        private <T> void install(Class<T> key, T service) {
            manager().put(key, service);
        }

        public ServiceRegistry manager() {
            return service;
        }
    }

}
