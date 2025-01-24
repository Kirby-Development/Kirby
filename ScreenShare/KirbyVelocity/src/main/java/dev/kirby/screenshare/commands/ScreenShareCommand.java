package dev.kirby.screenshare.commands;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.kirby.config.ConfigManager;
import dev.kirby.packet.registry.SendPacket;
import dev.kirby.screenshare.PlayerState;
import dev.kirby.screenshare.configuration.Config;
import dev.kirby.screenshare.packet.StartSession;
import dev.kirby.screenshare.packet.StatePacket;
import dev.kirby.screenshare.player.SSManager;
import dev.kirby.screenshare.player.SSPlayer;
import dev.kirby.screenshare.player.Session;
import dev.kirby.screenshare.utils.ServerUtils;

import java.util.List;

public class ScreenShareCommand extends SSCommand {

    private final Session.Manager sessionManager;


    public ScreenShareCommand(Session.Manager sessionManager, ConfigManager<Config> configManager, SSManager manager, ProxyServer server) {
        super(server, manager, configManager);
        this.sessionManager = sessionManager;
    }


    @Override
    protected void execute(Player p, String[] args) {
        Result result = result(p, args);
        if (result == null) {
            System.out.println("null result");
            return;
        }
        Config config = configManager.get();
        SSPlayer staff = result.staff();
        SSPlayer ssTarget = result.ssTarget();
        if (sessionManager.contains(staff.getSsId())) {
            p.sendMessage(config.getMessages().alreadyInSS(result.target().getUsername()));
            return;
        }

        final StatePacket packet;
        final int sessionId;
        final RegisteredServer ss = ServerUtils.getServer(server, config.getServers().getSs());
        if (sessionManager.contains(ssTarget.getSsId())) {
            staff.setSsId(sessionId = ssTarget.getSsId());
            sessionManager.getSession(sessionId).getDebug().add(staff);
            packet = new StatePacket(p.getUniqueId(), PlayerState.DEBUG, sessionId);
            staff.setPlayerState(PlayerState.DEBUG);
            System.out.println("session already exists, joining debug " + sessionId);
        } else {
            Session session = sessionManager.create(staff, ssTarget);
            sessionId = session.getId();
            staff.setSsId(sessionId);
            System.out.println("creating new session: " + sessionId);
            packet = new StatePacket(p.getUniqueId(), PlayerState.STAFF, sessionId);
            staff.setPlayerState(PlayerState.STAFF);
            p.createConnectionRequest(ss).fireAndForget();
        }

        SendPacket sendPacket = get(SendPacket.class);

        sendPacket.sendPacket(packet);
        sendPacket.sendPacket(new StatePacket(result.target().getUniqueId(), PlayerState.SUSPECT, sessionId));
        ssTarget.setPlayerState(PlayerState.SUSPECT);
        ssTarget.setSsId(sessionId);
        result.target().createConnectionRequest(ss).fireAndForget();

        sendPacket.sendPacket(new StartSession(sessionId));
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        return manager.getProfiles().stream().filter(player -> !player.isStaff()).map(SSPlayer::getPlayer).map(Player::getUsername).toList();
    }

}
