package dev.kirby.api.netty;

import dev.kirby.Utils;
import dev.kirby.api.plugin.KirbyPlugin;
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

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Consumer;

@Getter
public class NettyClient extends ChannelInitializer<Channel> {

    private final Bootstrap bootstrap;
    private final IPacketRegistry packetRegistry;
    private final EventRegistry eventRegistry = new EventRegistry();

    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    private final ClientEvents clientEvents;

    private final Runnable shutdownRunnable;

    public NettyClient(IPacketRegistry packetRegistry, Consumer<Future<? super Void>> doneCallback, KirbyPlugin kirby, Runnable shutdownHook) {
        this.packetRegistry = packetRegistry;
        eventRegistry.registerEvents(this.clientEvents = new ClientEvents(this, kirby));
        shutdownRunnable = shutdownHook;
        this.bootstrap = new Bootstrap()
                .option(ChannelOption.AUTO_READ, true)
                .group(workerGroup)
                .handler(this)
                .channel(NioSocketChannel.class);

        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));

        try {
            this.bootstrap.connect(new InetSocketAddress("127.0.0.1", 9900))
                    .awaitUninterruptibly().sync().addListener(doneCallback::accept);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public NettyClient(IPacketRegistry packetRegistry, Consumer<Future<? super Void>> doneCallback, String[] data, String license, Runnable shutdownHook) {
        this.packetRegistry = packetRegistry;
        eventRegistry.registerEvents(this.clientEvents = new ClientEvents(this, data, license));
        shutdownRunnable = shutdownHook;
        this.bootstrap = new Bootstrap()
                .option(ChannelOption.AUTO_READ, true)
                .group(workerGroup)
                .handler(this)
                .channel(NioSocketChannel.class);

        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));

        try {
            this.bootstrap.connect(new InetSocketAddress("127.0.0.1", 9900))
                    .awaitUninterruptibly().sync().addListener(doneCallback::accept);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        channel.pipeline().addLast(new PacketDecoder(packetRegistry), new PacketEncoder(packetRegistry),

                new PacketChannelInboundHandler(eventRegistry){
                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        ctx.channel().writeAndFlush(new LoginPacket(Utils.getData(), clientEvents.getData(), clientEvents.getLicense()));
                        super.channelActive(ctx);
                    }
                }

        );
    }

    public void shutdown() {
        try {
            workerGroup.shutdownGracefully().get();
            shutdownRunnable.run();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }



}