package dev.kirby.api.netty;

import dev.kirby.Utils;
import dev.kirby.netty.event.PacketSubscriber;
import dev.kirby.netty.io.Responder;
import dev.kirby.packet.*;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.UUID;

@Getter
public class ClientEvents {

    private final String[] data;
    public ClientEvents(String[] data) {
        this.data = data;
    }

    private Responder lastResponder;
    private ChannelHandlerContext lastCtx;

    @SneakyThrows
    @PacketSubscriber
    public void onPacketReceive(ConnectPacket packet, ChannelHandlerContext ctx, Responder responder) {
        lastCtx = ctx;
        lastResponder = responder;
        System.out.println("Received " + packet.getPacketName());
        responder.respond(new DataPacket(data));
        responder.respond(new LicensePacket("test", UUID.nameUUIDFromBytes(Utils.getBytes(data))));
    }

    @PacketSubscriber
    public void onPacketReceive(TextPacket packet, ChannelHandlerContext ctx, Responder responder) {
        lastCtx = ctx;
        lastResponder = responder;
        System.out.println("Received \"" + packet.getText() + "\"");
    }

    @PacketSubscriber
    public void onPacketReceive(Status.Packet packet, ChannelHandlerContext ctx, Responder responder) {
        lastCtx = ctx;
        lastResponder = responder;
        Status status = packet.getStatus();
        System.out.println("Received " + status);

        if (!status.valid()) {
            System.out.println("shutdown");
        }
    }
}
