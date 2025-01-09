package dev.kirby.api.netty;

import dev.kirby.Utils;
import dev.kirby.api.plugin.KirbyPlugin;
import dev.kirby.api.util.KirbyLogger;
import dev.kirby.netty.event.EventRegistry;
import dev.kirby.netty.handler.PacketChannelInboundHandler;
import dev.kirby.netty.handler.PacketDecoder;
import dev.kirby.netty.handler.PacketEncoder;
import dev.kirby.netty.registry.IPacketRegistry;
import dev.kirby.packet.LoginPacket;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

@Getter
public class NettyClient extends ChannelInitializer<Channel> {

    private final Bootstrap bootstrap;
    private final IPacketRegistry packetRegistry;
    private final EventRegistry eventRegistry = new EventRegistry();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    private final KirbyLogger logger;

    private final ClientEvents clientEvents;
    private final Runnable shutdownHook;

    private Channel channel;

    @Setter
    private String[] data;
    @Setter
    private String license;

    public NettyClient(IPacketRegistry packetRegistry, KirbyPlugin kirby, Runnable shutdownHook) {
        this(packetRegistry, shutdownHook, "KirbyLicense-" + kirby.getName());
        this.data = kirby.data();
        this.license = kirby.getConfig().getLicense();
    }

    public NettyClient(IPacketRegistry packetRegistry, Runnable shutdownHook, String loggerName) {
        this.packetRegistry = packetRegistry;
        this.shutdownHook = shutdownHook;
        this.logger = new KirbyLogger(loggerName);
        this.eventRegistry.registerEvents(this.clientEvents = new ClientEvents(this));
        this.bootstrap = new Bootstrap()
                .option(ChannelOption.AUTO_READ, true)
                .group(workerGroup)
                .handler(this)
                .channel(NioSocketChannel.class);

    }

    public void connect() {
        try {
            ChannelFuture future = this.bootstrap.connect(new InetSocketAddress("127.0.0.1", 9900)).sync();
            future.addListener((ChannelFutureListener) future1 -> {
                if (future1.isSuccess()) {
                    logger.info("Connected!");
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
    protected void initChannel(Channel channel) throws Exception {
        (this.channel = channel).pipeline().addLast(new PacketDecoder(packetRegistry), new PacketEncoder(packetRegistry),
                new PacketChannelInboundHandler(eventRegistry) {
                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        ctx.channel().writeAndFlush(new LoginPacket(Utils.getData(), data, license));
                        super.channelActive(ctx);
                    }
                }
        );
    }


    public void setInfo(String[] data, @Nullable String license) {
        setData(data);
        setLicense(license);
    }

    public void shutdown() {
        shutdownHook.run();
        try {
            workerGroup.shutdownGracefully().sync();
        } catch (InterruptedException e) {
            logger.error("Failed to shutdown", e);
        } finally {
            workerGroup.shutdownGracefully(0, 0, TimeUnit.SECONDS).syncUninterruptibly();
        }
    }


}
