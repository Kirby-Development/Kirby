package dev.kirby.server;

import dev.kirby.netty.event.EventRegistry;
import dev.kirby.netty.handler.PacketChannelInboundHandler;
import dev.kirby.netty.handler.PacketDecoder;
import dev.kirby.netty.handler.PacketEncoder;
import dev.kirby.netty.registry.IPacketRegistry;
import dev.kirby.packet.ConnectPacket;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Getter;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Consumer;


@Getter
public class NettyServer extends ChannelInitializer<Channel> {

    private final ServerBootstrap bootstrap;
    private final IPacketRegistry packetRegistry;

    private final EventLoopGroup parentGroup = new NioEventLoopGroup();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    private final EventRegistry eventRegistry = new EventRegistry();

    private Channel connectedChannel;


    public NettyServer(IPacketRegistry packetRegistry, Consumer<Future<? super Void>> doneCallback, Consumer<EventRegistry> eventRegistryCallback) {
        this.packetRegistry = packetRegistry;
        this.bootstrap = new ServerBootstrap()
                .option(ChannelOption.AUTO_READ, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .group(parentGroup, workerGroup)
                .childHandler(this)
                .channel(NioServerSocketChannel.class);

        eventRegistryCallback.accept(eventRegistry);


        try {
            this.bootstrap.bind(new InetSocketAddress("127.0.0.1", 9900))
                    .awaitUninterruptibly().sync().addListener(doneCallback::accept);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initChannel(Channel channel) {
        channel.pipeline().addLast(new PacketDecoder(packetRegistry), new PacketEncoder(packetRegistry),new PacketChannelInboundHandler(eventRegistry));

        (this.connectedChannel = channel).writeAndFlush(new ConnectPacket());
        String ip = channel.remoteAddress().toString();
        System.out.println("sent connect packet to " + ip);
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