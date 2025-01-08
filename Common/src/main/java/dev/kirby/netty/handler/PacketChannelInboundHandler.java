package dev.kirby.netty.handler;

import dev.kirby.netty.Packet;
import dev.kirby.netty.event.EventRegistry;
import dev.kirby.netty.response.RespondingPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class PacketChannelInboundHandler extends SimpleChannelInboundHandler<Packet> {

    private final EventRegistry eventRegistry;

    public PacketChannelInboundHandler(EventRegistry eventRegistry) {
        this.eventRegistry = eventRegistry;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet packet) throws Exception {
        RespondingPacket.callReceive(packet);
        eventRegistry.invoke(packet, channelHandlerContext);
    }


}