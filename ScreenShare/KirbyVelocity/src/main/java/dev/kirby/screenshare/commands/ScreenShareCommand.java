package dev.kirby.screenshare.commands;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.kirby.screenshare.PlayerState;
import dev.kirby.packet.registry.PacketSender;
import dev.kirby.screenshare.packet.StatePacket;
import dev.kirby.screenshare.player.SSManager;
import dev.kirby.screenshare.player.Session;
import dev.kirby.screenshare.utils.ServerUtils;
import net.kyori.adventure.text.Component;

public class ScreenShareCommand extends SSCommand {

    private final Session.Manager sessionManager;
    private final PacketSender packetSender;

    public ScreenShareCommand(Session.Manager sessionManager, SSManager manager, ProxyServer server) {
        super(server, manager);
        this.sessionManager = sessionManager;
        this.packetSender = get(PacketSender.class);
    }

    // /ss negro

    @Override
    protected void execute(Player p, String[] args) {
        Result result = result(p, args);
        if (result == null) return;
        if (sessionManager.contains(result.staff().getSsId())) {
            p.sendMessage(Component.text(" already in a ss"));
            //todo already in a ss
            return;
        }

        final StatePacket packet;
        final int sessionId;
        final RegisteredServer ss = ServerUtils.getServer("ss");
        if (sessionManager.contains(result.ssTarget().getSsId())) {
            result.staff().setSsId(sessionId = result.ssTarget().getSsId());
            sessionManager.getSession(sessionId).getDebug().add(result.staff());
            packet = new StatePacket(p.getUniqueId(), PlayerState.DEBUG, sessionId);
        } else {
            Session session = sessionManager.create(result.staff(), result.ssTarget());
            sessionId = session.getId();
            packet = new StatePacket(p.getUniqueId(), PlayerState.STAFF, sessionId);

            p.createConnectionRequest(ss).fireAndForget();
        }

        packetSender.sendPacket(packet);
        packetSender.sendPacket(new StatePacket(result.target().getUniqueId(), PlayerState.SUSPECT, sessionId));
        result.target().createConnectionRequest(ss).fireAndForget();
    }


}
