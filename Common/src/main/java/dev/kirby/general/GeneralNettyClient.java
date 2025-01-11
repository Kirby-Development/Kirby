package dev.kirby.general;

import dev.kirby.KirbyLogger;
import dev.kirby.netty.event.EventRegistry;
import dev.kirby.netty.handler.PacketChannelInboundHandler;
import dev.kirby.netty.handler.PacketDecoder;
import dev.kirby.netty.handler.PacketEncoder;
import dev.kirby.netty.registry.IPacketRegistry;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import lombok.Setter;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Getter
public class GeneralNettyClient extends ChannelInitializer<Channel> {

    private final Bootstrap bootstrap;
    private final IPacketRegistry packetRegistry;
    private final EventRegistry eventRegistry = new EventRegistry();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    private final KirbyLogger logger;

    private final Runnable shutdownHook;

    @Setter
    private Consumer<ChannelHandlerContext> channelActiveAction;

    private Channel channel;

    public GeneralNettyClient(IPacketRegistry packetRegistry, Runnable shutdownHook, String loggerName) {
        this.packetRegistry = packetRegistry;
        this.shutdownHook = shutdownHook;
        this.logger = new KirbyLogger(loggerName);
        this.bootstrap = new Bootstrap()
                .option(ChannelOption.AUTO_READ, true)
                .group(workerGroup)
                .handler(this)
                .channel(NioSocketChannel.class);

    }

    public void connect(String host, int port) {
        try {
            ChannelFuture future = this.bootstrap.connect(new InetSocketAddress(host,port)).sync();
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
    protected void initChannel(Channel channel) {
        (this.channel = channel).pipeline().addLast(new PacketDecoder(packetRegistry), new PacketEncoder(packetRegistry),
                new PacketChannelInboundHandler(eventRegistry) {
                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        if (channelActiveAction != null) channelActiveAction.accept(ctx);
                        super.channelActive(ctx);
                    }
                }
        );
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
