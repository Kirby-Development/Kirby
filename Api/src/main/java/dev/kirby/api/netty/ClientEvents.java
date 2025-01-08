package dev.kirby.api.netty;

import dev.kirby.netty.event.PacketSubscriber;
import dev.kirby.netty.io.Responder;
import dev.kirby.packet.ConnectPacket;
import dev.kirby.packet.LoginPacket;
import dev.kirby.packet.Status;
import dev.kirby.packet.TextPacket;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.SneakyThrows;

@Getter
public class ClientEvents {

    private final String[] data, pluginData;
    public ClientEvents(String[] data, String[] pluginData) {
        this.data = data;
        this.pluginData = pluginData;
    }

    private Responder lastResponder;
    private ChannelHandlerContext lastCtx;

    @SneakyThrows
    @PacketSubscriber
    public void onPacketReceive(ConnectPacket packet, ChannelHandlerContext ctx, Responder responder) {
        lastCtx = ctx;
        lastResponder = responder;
        System.out.println("Received " + packet.getPacketName());
        responder.respond(new LoginPacket(data,pluginData,"test"));
    }

    @PacketSubscriber
    public void onPacketReceive(TextPacket packet, ChannelHandlerContext ctx, Responder responder) {
        lastCtx = ctx;
        lastResponder = responder;
        System.out.println("Received \"" + packet.getText() + "\"");
    }

    @PacketSubscriber
    public void onPacketReceive(Status.ResponsePacket packet, ChannelHandlerContext ctx, Responder responder) {
        lastCtx = ctx;
        lastResponder = responder;
        Status status = packet.getStatus();
        System.out.println("Received " + status);

        if (!status.valid()) {
            System.out.println("shutdown");
        }
    }
}
