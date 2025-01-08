package dev.kirby.server;

import dev.kirby.Utils;
import dev.kirby.checker.LoginChecker;
import dev.kirby.database.DatabaseManager;
import dev.kirby.netty.event.PacketSubscriber;
import dev.kirby.netty.io.Responder;
import dev.kirby.packet.LoginPacket;
import dev.kirby.packet.Status;
import dev.kirby.packet.TextPacket;
import io.netty.channel.ChannelHandlerContext;

public class ServerEvents {

    private final DatabaseManager databaseManager;

    public ServerEvents(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @PacketSubscriber
    public void onPacketReceive(LoginPacket packet, ChannelHandlerContext ctx, Responder responder) {
        String ip = Utils.getIp(ctx);
        System.out.println("Received " + packet.getPacketName() + " from " + ip);
        Status status = LoginChecker.get(databaseManager).check(packet, ip.split(":")[0]);
        System.out.println(status.name());
        responder.respond(new Status.ResponsePacket(status));
    }

    @PacketSubscriber
    public void onPacketReceive(TextPacket packet, ChannelHandlerContext ctx, Responder responder) {
        System.out.println("Received \"" + packet.getText() + "\" from " + Utils.getIp(ctx));
    }
}