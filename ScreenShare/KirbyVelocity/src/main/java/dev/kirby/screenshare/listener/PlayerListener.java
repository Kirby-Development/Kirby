package dev.kirby.screenshare.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.kirby.config.ConfigManager;
import dev.kirby.packet.registry.SendPacket;
import dev.kirby.screenshare.PlayerState;
import dev.kirby.screenshare.configuration.Config;
import dev.kirby.screenshare.player.SSManager;
import dev.kirby.screenshare.player.SSPlayer;
import dev.kirby.screenshare.player.Session;
import dev.kirby.screenshare.utils.VelocityService;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;

import static dev.kirby.screenshare.commands.ClearCommand.finishSS;

public class PlayerListener implements VelocityService {

    private final ProxyServer server;
    private final ConfigManager<Config> config;
    private final SSManager manager;
    private final Session.Manager sessionManager;

    public PlayerListener(ProxyServer server, ConfigManager<Config> config, SSManager manager, Session.Manager sessionManager) {
        this.server = server;
        this.config = config;
        this.manager = manager;
        this.sessionManager = sessionManager;
    }

    @Subscribe
    public void onPlayerJoin(ServerConnectedEvent event) {
        Player player = event.getPlayer();
        String username = player.getUsername();
        System.out.println("joined " + username);

        String serverName = event.getServer().getServerInfo().getName();
        Config config = this.config.get();


        SSPlayer profile;
        if (event.getPreviousServer().isEmpty()) profile = manager.createProfile(player);
        else if (!serverName.equals(config.getServers().getSs())) {
            System.out.println(username + "'s server is " + serverName);
            return;
        } else profile = manager.getProfile(player);

        if (profile == null) {
            System.out.println(username + "'s profile is null");
            return;
        }

        Session session = sessionManager.getProfile(profile.getSsId());
        if (session == null) {
            System.out.println(username + "'s session not found");
            return;
        }

        SSPlayer sus = session.getSuspect();
        SSPlayer stf = session.getStaff();

        final String susName = sus.getPlayer().getUsername();

        Config.Message.Param staffParam = new Config.Message.Param("%staff%", stf.getPlayer().getUsername());
        Config.Message.Param susParam = new Config.Message.Param("%suspect%", susName);

        Config.Titles.TitleState start = config.getTitles().getStart();
        if (!profile.isStaff()) {
            System.out.println("sending title ecc to " + username);
            start.getSus().send(player, staffParam, susParam);
            return;
        }

        if (profile.getPlayerState().equals(PlayerState.DEBUG)) {
            System.out.println(username + " is debug");
            start.getDebug().send(player, staffParam, susParam);
            return;
        }

        Config.Buttons buttons = config.getButtons();

        Config.Ban ban = config.getBan();

        System.out.println("sending buttons to " + username);

        start.getStaff().send(player, staffParam, susParam);

        long sessionId = session.getId();

        for (Config.Buttons.Button button : buttons.getButtons()) {
            player.sendMessage(button.text(staffParam, susParam)
                    .hoverEvent(HoverEvent.showText(button.hover(staffParam, susParam)))
                    .clickEvent(ClickEvent.callback(audience -> {
                        String command = ban.getCommand()
                                .replace("%player%", susName)
                                .replace("%uuid%", sus.getUuid().toString())
                                .replace("%cause%", button.cause(staffParam, susParam))
                                .replace("%duration%", button.getDuration());
                        boolean staff = ban.getWho().isStaff();
                        server.getCommandManager().executeImmediatelyAsync(staff ? player : server.getConsoleCommandSource(), command);
                        System.out.println("executing " + command);
                        finishSS(sessionManager, sessionId, server, config, get(SendPacket.class));
                    })));
        }

        Config.Buttons.Button button = buttons.getClean();
        player.sendMessage(button.text(staffParam, susParam)
                .hoverEvent(HoverEvent.showText(button.hover(staffParam, susParam)))
                .clickEvent(ClickEvent.callback(audience -> server.getCommandManager().executeImmediatelyAsync(player, "clean " + susName))));

    }

    @Subscribe
    public void onPlayerQuit(DisconnectEvent event) {
        manager.removeProfile(event.getPlayer());
    }
}
