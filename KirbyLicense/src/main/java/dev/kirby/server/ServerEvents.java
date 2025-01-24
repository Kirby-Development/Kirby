package dev.kirby.server;

import dev.kirby.ServerLauncher;
import dev.kirby.checker.LoginChecker;
import dev.kirby.netty.event.PacketSubscriber;
import dev.kirby.netty.io.Responder;
import dev.kirby.packet.empty.ShutdownPacket;
import dev.kirby.packet.registration.Status;
import dev.kirby.packet.text.TextPacket;
import dev.kirby.packet.registration.LoginPacket;
import dev.kirby.packet.registration.LogoutPacket;
import dev.kirby.resources.ClientManager;
import dev.kirby.resources.Connection;
import dev.kirby.utils.Utils;
import io.netty.channel.ChannelHandlerContext;

public class ServerEvents {

    private final ServerLauncher serverLauncher;
    private final ClientManager clientManager;

    public ServerEvents(ServerLauncher serverLauncher) {
        this.serverLauncher = serverLauncher;
        clientManager = serverLauncher.getClientManager();
    }

    public void onLogin(LoginPacket packet, ChannelHandlerContext ctx, Responder responder) {
        final String ip = Utils.getIp(ctx);
        //todo webhook
        System.out.println("Received " + packet.getPacketName() + " from " + ip);

        final String cleanIp = ip.split(":")[0];

        final Connection connection = clientManager.get(packet);

        boolean valid = clientManager.attempt(packet, cleanIp);

        if (!valid) {
            responder.respond(new Status.ResponsePacket(Status.TOO_MANY_ATTEMPTS));
            responder.respond(new ShutdownPacket());
            connection.setValid(false);
            clientManager.update(connection);
            System.out.println("Failed to connect: to many attempts");
            return;
        }

        Status status = LoginChecker.get(serverLauncher).check(packet, cleanIp);
        System.out.println(status.name());
        responder.respond(new Status.ResponsePacket(status));

        if (status.valid()) {
            clientManager.connect(connection, cleanIp);
            return;
        }

        responder.respond(new ShutdownPacket());
        connection.setValid(false);
        connection.increase(serverLauncher.getConfig().getMaxAttempts());
        clientManager.update(connection);
    }

    @PacketSubscriber
    public void onLogout(LogoutPacket packet, Responder responder, ChannelHandlerContext ctx) {
        clientManager.disconnect(packet);
    }

    @PacketSubscriber
    public void onText(TextPacket packet, ChannelHandlerContext ctx, Responder responder) {
        System.out.println("Received \"" + packet.getText() + "\" from " + Utils.getIp(ctx));
    }
}