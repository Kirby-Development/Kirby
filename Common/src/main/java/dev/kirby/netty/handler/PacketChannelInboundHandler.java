package dev.kirby.netty.handler;

import dev.kirby.netty.Packet;
import dev.kirby.netty.event.EventRegistry;
import dev.kirby.netty.response.RespondingPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class PacketChannelInboundHandler extends SimpleChannelInboundHandler<Packet> {

    private final boolean DEBUG = false;

    private final EventRegistry eventRegistry;

    public PacketChannelInboundHandler(EventRegistry eventRegistry) {
        this.eventRegistry = eventRegistry;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet packet) throws Exception {
        RespondingPacket.callReceive(packet);
        eventRegistry.invoke(packet, channelHandlerContext);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (DEBUG) {
            System.out.println("exceptionCaught: " + cause.getMessage());
            cause.printStackTrace(System.out);
        }
    }


}