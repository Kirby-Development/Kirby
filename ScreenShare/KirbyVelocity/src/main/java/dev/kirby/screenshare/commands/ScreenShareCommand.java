package dev.kirby.screenshare.commands;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.kirby.config.ConfigManager;
import dev.kirby.packet.registry.SendPacket;
import dev.kirby.screenshare.PlayerState;
import dev.kirby.screenshare.configuration.Config;
import dev.kirby.screenshare.packet.StatePacket;
import dev.kirby.screenshare.player.SSManager;
import dev.kirby.screenshare.player.Session;
import dev.kirby.screenshare.utils.ServerUtils;

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
        if (sessionManager.contains(result.staff().getSsId())) {
            p.sendMessage(config.getMessages().alreadyInSS(result.target().getUsername()));
            return;
        }

        final StatePacket packet;
        final int sessionId;
        final RegisteredServer ss = ServerUtils.getServer(server, config.getServers().getSs());
        if (sessionManager.contains(result.ssTarget().getSsId())) {
            result.staff().setSsId(sessionId = result.ssTarget().getSsId());
            sessionManager.getSession(sessionId).getDebug().add(result.staff());
            packet = new StatePacket(p.getUniqueId(), PlayerState.DEBUG, sessionId);
            System.out.println("session already exists, joining debug " + sessionId);
        } else {
            Session session = sessionManager.create(result.staff(), result.ssTarget());
            sessionId = session.getId();
            result.staff().setSsId(sessionId);
            System.out.println("creating new session: " + sessionId);
            packet = new StatePacket(p.getUniqueId(), PlayerState.STAFF, sessionId);
            p.createConnectionRequest(ss).fireAndForget();
        }

        SendPacket sendPacket = get(SendPacket.class);

        sendPacket.sendPacket(packet);
        sendPacket.sendPacket(new StatePacket(result.target().getUniqueId(), PlayerState.SUSPECT, sessionId));
        result.ssTarget().setSsId(sessionId);
        result.target().createConnectionRequest(ss).fireAndForget();
    }


}
