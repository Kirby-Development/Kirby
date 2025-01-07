package dev.kirby.api.netty;

import dev.kirby.netty.Packet;
import dev.kirby.netty.event.EventRegistry;
import dev.kirby.netty.handler.PacketChannelInboundHandler;
import dev.kirby.netty.handler.PacketDecoder;
import dev.kirby.netty.handler.PacketEncoder;
import dev.kirby.netty.io.Responder;
import dev.kirby.netty.registry.IPacketRegistry;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Function;

@Getter
public class NettyClient extends ChannelInitializer<Channel> {

    private final Bootstrap bootstrap;
    private final IPacketRegistry packetRegistry;
    private final EventRegistry eventRegistry = new EventRegistry();

    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    private final ClientEvents clientEvents;

    public NettyClient(IPacketRegistry packetRegistry, Consumer<Future<? super Void>> doneCallback, ClientEvents clientEvents) {
        this.packetRegistry = packetRegistry;
        eventRegistry.registerEvents(this.clientEvents = clientEvents);
        this.bootstrap = new Bootstrap()
                .option(ChannelOption.AUTO_READ, true)
                .group(workerGroup)
                .handler(this)
                .channel(NioSocketChannel.class);

        try {
            this.bootstrap.connect(new InetSocketAddress("127.0.0.1", 9900))
                    .awaitUninterruptibly().sync().addListener(doneCallback::accept);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        channel.pipeline().addLast(new PacketDecoder(packetRegistry), new PacketEncoder(packetRegistry), new PacketChannelInboundHandler(eventRegistry));
    }

    public void shutdown() {
        try {
            workerGroup.shutdownGracefully().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public <T extends Packet> void sendPacket(Function<ChannelHandlerContext, T> packet) {
        Responder responder = clientEvents.getLastResponder();
        if (responder == null) return;
        ChannelHandlerContext ctx = clientEvents.getLastCtx();
        if (ctx == null) return;
        T apply = packet.apply(ctx);
        responder.respond(apply);
        System.out.println("sent packet " + apply.getPacketName());
    }




}