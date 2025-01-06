package dev.kirby.server;

import dev.kirby.hwid.HwidCalculator;
import dev.kirby.hwid.HwidChecker;
import dev.kirby.netty.event.EventRegistry;
import dev.kirby.netty.event.PacketSubscriber;
import dev.kirby.netty.handler.PacketChannelInboundHandler;
import dev.kirby.netty.handler.PacketDecoder;
import dev.kirby.netty.handler.PacketEncoder;
import dev.kirby.netty.io.Responder;
import dev.kirby.netty.registry.IPacketRegistry;
import dev.kirby.packet.ConnectPacket;
import dev.kirby.packet.DataPacket;
import dev.kirby.packet.Status;
import dev.kirby.packet.TextPacket;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Consumer;

public class NettyServer extends ChannelInitializer<Channel> {

    private final ServerBootstrap bootstrap;
    private final IPacketRegistry packetRegistry;

    private final EventLoopGroup parentGroup = new NioEventLoopGroup();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    private final EventRegistry eventRegistry = new EventRegistry();

    private Channel connectedChannel;

    public NettyServer(IPacketRegistry packetRegistry, Consumer<Future<? super Void>> doneCallback) {
        this.packetRegistry = packetRegistry;
        this.bootstrap = new ServerBootstrap()
                .option(ChannelOption.AUTO_READ, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .group(parentGroup, workerGroup)
                .childHandler(this)
                .channel(NioServerSocketChannel.class);

        eventRegistry.registerEvents(new Object() {

            @PacketSubscriber
            public void onPacketReceive(DataPacket packet, ChannelHandlerContext ctx, Responder responder) {
                System.out.println("Received " + packet.getPacketName() + " from " + ctx.channel().remoteAddress().toString());
                String hwid = HwidCalculator.get().calculate(packet.getData());
                Status status = HwidChecker.get().check(hwid);
                System.out.println(status.name());
                responder.respond(new Status.Packet(status));
            }


            @PacketSubscriber
            public void onPacketReceive(TextPacket packet, ChannelHandlerContext ctx, Responder responder) {
                System.out.println("Received " + packet.getPacketName() + " from " + ctx.channel().remoteAddress().toString());
                //todo LicenseChecker
                Status status = HwidChecker.get().check(packet.getText());
                System.out.println(status.name());
                responder.respond(new Status.Packet(status));
            }
        });

        try {
            this.bootstrap.bind(new InetSocketAddress("127.0.0.1", 1234))
                    .awaitUninterruptibly().sync().addListener(doneCallback::accept);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initChannel(Channel channel) {
        channel.pipeline().addLast(new PacketDecoder(packetRegistry), new PacketEncoder(packetRegistry),new PacketChannelInboundHandler(eventRegistry));

        (this.connectedChannel = channel).writeAndFlush(new ConnectPacket());
        System.out.println("sent connect packet to " + channel.remoteAddress().toString());
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