package dev.kirby.screenshare.commands;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.kirby.config.ConfigManager;
import dev.kirby.packet.registry.SendPacket;
import dev.kirby.screenshare.PlayerState;
import dev.kirby.screenshare.configuration.Config;
import dev.kirby.screenshare.packet.EndSession;
import dev.kirby.screenshare.packet.StatePacket;
import dev.kirby.screenshare.player.SSManager;
import dev.kirby.screenshare.player.SSPlayer;
import dev.kirby.screenshare.player.Session;
import dev.kirby.screenshare.utils.ServerUtils;

import java.util.List;

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
        final long sessionId = result.ssTarget().getSsId();
        Config config = configManager.get();
        if (!sessionManager.contains(sessionId)) {
            p.sendMessage(config.getMessages().notInSS(result.target().getUsername()));
            return;
        }


        finishSS(sessionManager, sessionId, server, config, get(SendPacket.class));
    }

    public static void finishSS(Session.Manager sessionManager, long sessionId, ProxyServer server, Config config, SendPacket sendPacket) {
        Session session = sessionManager.getProfile(sessionId);
        SSPlayer sus = session.getSuspect();
        SSPlayer stf = session.getStaff();

        final RegisteredServer ss = ServerUtils.getServer(server, config.getServers().getHub());

        final String susName = sus.getPlayer().getUsername();

        Config.Message.Param staffParam = new Config.Message.Param("%staff%", stf.getPlayer().getUsername());
        Config.Message.Param susParam = new Config.Message.Param("%suspect%", susName);


        for (SSPlayer player : session.getAll()) {
            player.setSsId(-1);

            player.setPlayerState(PlayerState.NONE);
            sendPacket.sendPacket(new StatePacket(player.getUuid(), PlayerState.NONE, -1));

            Player p1 = player.getPlayer();
            if (p1 == null || !p1.isActive()) continue;
            Config.Titles.TitleState end = config.getTitles().getEnd();
            switch (player.getPlayerState()) {
                case DEBUG -> end.getDebug().send(p1, staffParam, susParam);
                case STAFF -> end.getStaff().send(p1, staffParam, susParam);
                case SUSPECT -> end.getSus().send(p1, staffParam, susParam);
            }
            p1.createConnectionRequest(ss).fireAndForget();
        }

        sessionManager.removeProfile(sessionId);
        sendPacket.sendPacket(new EndSession(sessionId));
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        return sessionManager.getProfiles().stream().map(Session::getSuspect).map(SSPlayer::getPlayer).map(Player::getUsername).toList();
    }
}
