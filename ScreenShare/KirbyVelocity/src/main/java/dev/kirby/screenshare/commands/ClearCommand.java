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
import dev.kirby.screenshare.player.SSPlayer;
import dev.kirby.screenshare.player.Session;
import dev.kirby.screenshare.utils.ServerUtils;

public class ClearCommand extends SSCommand {

    private final Session.Manager sessionManager;
    private final ConfigManager<Config> configManager;

    public ClearCommand(Session.Manager sessionManager, ConfigManager<Config> configManager, SSManager manager, ProxyServer server) {
        super(server, manager, configManager);
        this.sessionManager = sessionManager;
        this.configManager = configManager;
    }


    @Override
    protected void execute(Player p, String[] args) {
        Result result = result(p, args);
        if (result == null) return;
        final int sessionId = result.ssTarget().getSsId();
        Config config = configManager.get();
        if (!sessionManager.contains(sessionId)) {
            p.sendMessage(config.getMessages().notInSS(result.target().getUsername()));
            return;
        }

        final RegisteredServer ss = ServerUtils.getServer(server, config.getServers().getHub());


        SendPacket sendPacket = get(SendPacket.class);
        for (SSPlayer player : sessionManager.getSession(sessionId).getAll()) {
            player.setSsId(-1);
            Player p1 = player.getPlayer();
            sendPacket.sendPacket(new StatePacket(player.getUuid(), PlayerState.NONE, -1));
            p1.createConnectionRequest(ss).fireAndForget();
        }

        sessionManager.delete(sessionId);

    }
}
