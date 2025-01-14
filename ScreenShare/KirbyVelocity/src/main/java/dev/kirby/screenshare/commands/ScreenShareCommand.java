package dev.kirby.screenshare.commands;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.kirby.screenshare.PlayerState;
import dev.kirby.screenshare.ServerUtils;
import dev.kirby.screenshare.packet.PacketSender;
import dev.kirby.screenshare.packet.StatePacket;
import dev.kirby.screenshare.player.SSManager;
import dev.kirby.screenshare.player.SSPlayer;
import dev.kirby.screenshare.player.Session;

import java.util.Optional;

public class ScreenShareCommand extends SSCommand {

    private final Session.Manager sessionManager;
    private final SSManager manager;
    private final ProxyServer server;
    private final PacketSender packetSender;

    public ScreenShareCommand(Session.Manager sessionManager, SSManager manager, ProxyServer server) {
        this.sessionManager = sessionManager;
        this.manager = manager;
        this.server = server;
        this.packetSender = get(PacketSender.class);
    }

    // /ss negro

    @Override
    protected void execute(Player p, String[] args) {
        if (args.length == 0) {
            //todo not enouh args
            return;
        }
        Optional<Player> optional = server.getPlayer(args[0]);
        if (optional.isEmpty()) {
            //todo player not found
            return;
        }
        SSPlayer staff = manager.getProfile(p);
        if (staff == null) return;
        Player target = optional.get();
        SSPlayer ssTarget = manager.getProfile(target);
        if (ssTarget == null) return;
        if (sessionManager.contains(staff.getSsId())) {
            //todo already in a ss
            return;
        }

        final StatePacket packet;
        int sessionId = ssTarget.getSsId();
        if (sessionManager.contains(sessionId)) {
            staff.setSsId(sessionId);
            Session session = sessionManager.getSession(sessionId);
            session.getDebug().add(staff);
            packet = new StatePacket(p.getUniqueId(), PlayerState.DEBUG, sessionId);
        } else {
            Session session = sessionManager.create(staff, ssTarget);
            sessionId = session.getId();
            packet = new StatePacket(p.getUniqueId(), PlayerState.STAFF, sessionId);

            p.createConnectionRequest(ServerUtils.getServer("ss")).fireAndForget();
        }

        packetSender.sendPacket(packet);
        packetSender.sendPacket(new StatePacket(target.getUniqueId(), PlayerState.SUSPECT, sessionId));
        target.createConnectionRequest(ServerUtils.getServer("ss")).fireAndForget();
    }
}
