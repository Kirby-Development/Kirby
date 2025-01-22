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
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;


@Getter
public class GeneralNettyServer extends ChannelInitializer<Channel> {

    private final ServerBootstrap bootstrap;
    private final IPacketRegistry packetRegistry;

    private final EventLoopGroup parentGroup = new NioEventLoopGroup();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    private final EventRegistry eventRegistry = new EventRegistry();

    private final PacketSender packetSender;

    private final Consumer<Channel> connect;
    private Channel connectedChannel;

    public GeneralNettyServer(IPacketRegistry packetRegistry, Consumer<Channel> connect, ServiceRegistry serviceRegistry) {
        this.packetRegistry = packetRegistry;
        this.connect = connect;
        this.bootstrap = new ServerBootstrap()
                .option(ChannelOption.AUTO_READ, true)
                //.option(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .group(parentGroup, workerGroup)
                .childHandler(this)
                .channel(NioServerSocketChannel.class);
        packetSender = new PacketSender(serviceRegistry);
        eventRegistry.registerEvents(packetSender);
    }

    public void bind(int port) {
        try {
            this.bootstrap.bind(port)
                    .awaitUninterruptibly().sync().addListener((ChannelFutureListener) future1 -> {
                        if (future1.isSuccess()) {
                            System.out.println("Started!");
                            return;
                        }
                        System.out.println("Starting failed!");
                        shutdown();
                    });
        } catch (InterruptedException e) {
            System.out.println("Starting failed! " + e);
            shutdown();
        }

    }

    @Override
    protected void initChannel(Channel channel) {
        channel.pipeline().addLast(new PacketDecoder(packetRegistry), new PacketEncoder(packetRegistry), new PacketChannelInboundHandler(eventRegistry));
        this.connectedChannel = channel;
        connect.accept(connectedChannel);
    }

    public void shutdown() {
        packetSender.service.destroy();
        try {
            parentGroup.shutdownGracefully().get();
            workerGroup.shutdownGracefully().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Setter
    @Getter
    public static class PacketSender {

        private final ServiceRegistry service;

        public PacketSender(ServiceRegistry service) {
            this.service = service;
        }

        public void sendPacket(final long id, final Packet packet) {
            if (id == -1){
                sendPacket(packet);
                return;
            }
            ChannelHandlerContext responder = channels.get(id);
            if (responder == null) return;
            responder.writeAndFlush(packet);
        }

        private final Map<Long, ChannelHandlerContext> channels = new ConcurrentHashMap<>();

        @PacketSubscriber
        public void onConnect(final ConnectPacket packet, final ChannelHandlerContext ctx) {
            final long id = packet.getSessionId();
            channels.put(id, ctx);
            install(SendPacket.class, this::sendPacket);
        }

        private void sendPacket(final Packet packet) {
            channels.values().forEach(resp -> resp.writeAndFlush(packet));
        }

        private <T> void install(Class<T> key, T service) {
            manager().put(key, service);
        }

        public ServiceRegistry manager() {
            return service;
        }
    }

}