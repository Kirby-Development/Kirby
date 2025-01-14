package dev.kirby.general;

import dev.kirby.netty.event.EventRegistry;
import dev.kirby.netty.handler.PacketChannelInboundHandler;
import dev.kirby.netty.handler.PacketDecoder;
import dev.kirby.netty.handler.PacketEncoder;
import dev.kirby.netty.registry.IPacketRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Getter;

import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;


@Getter
public class GeneralNettyServer extends ChannelInitializer<Channel> {

    private final ServerBootstrap bootstrap;
    private final IPacketRegistry packetRegistry;

    private final EventLoopGroup parentGroup = new NioEventLoopGroup();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    private final EventRegistry eventRegistry = new EventRegistry();

    private final Consumer<Channel> connect;
    private Channel connectedChannel;

    public GeneralNettyServer(IPacketRegistry packetRegistry, Consumer<Channel> connect) {
        this.packetRegistry = packetRegistry;
        this.connect = connect;
        this.bootstrap = new ServerBootstrap()
                .option(ChannelOption.AUTO_READ, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .group(parentGroup, workerGroup)
                .childHandler(this)
                .channel(NioServerSocketChannel.class);
    }

    public void bind(int port) {
        try {
            this.bootstrap.bind(port)
                    .awaitUninterruptibly().sync().addListener((ChannelFutureListener) future1 -> {
                        if (future1.isSuccess()) {
                            System.out.println("Connected!");
                            return;
                        }
                        System.out.println("Connect failed!");
                        shutdown();
                    });
        } catch (InterruptedException e) {
            System.out.println("Connect failed! " + e);
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
        try {
            parentGroup.shutdownGracefully().get();
            workerGroup.shutdownGracefully().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

}