package dev.kirby.api.netty;

import dev.kirby.netty.event.PacketSubscriber;
import dev.kirby.netty.io.Responder;
import dev.kirby.packet.*;
import io.netty.channel.ChannelHandlerContext;

import java.util.Arrays;
import java.util.UUID;

public class ClientEvents {

    private final Object[] data;
    public ClientEvents(Object[] data) {
        this.data = data;
    }

    @PacketSubscriber
    public void onPacketReceive(ConnectPacket packet, ChannelHandlerContext ctx, Responder responder) {
        System.out.println("Received " + packet.getPacketName() + " from " + ctx.channel().remoteAddress().toString());
        responder.respond(new DataPacket(data));
        responder.respond(new LicensePacket("license", UUID.nameUUIDFromBytes(Arrays.toString(data).getBytes())));
    }

    @PacketSubscriber
    public void onPacketReceive(TextPacket packet, ChannelHandlerContext ctx) {
        System.out.println("Received " + packet.getText() + " from " + ctx.channel().remoteAddress().toString());
    }

    @PacketSubscriber
    public void onPacketReceive(Status.Packet packet, ChannelHandlerContext ctx) {
        Status status = packet.getStatus();
        System.out.println("Received " + status + " from " + ctx.channel().remoteAddress().toString());

        if (!status.valid()) {
            System.out.println("shutdown");
        }
    }
}
