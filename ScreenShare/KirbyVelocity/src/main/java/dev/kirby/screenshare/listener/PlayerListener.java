package dev.kirby.screenshare.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.kirby.config.ConfigManager;
import dev.kirby.screenshare.configuration.Config;
import dev.kirby.screenshare.player.SSManager;
import dev.kirby.screenshare.player.SSPlayer;
import dev.kirby.screenshare.player.Session;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;

public class PlayerListener {

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
        SSPlayer profile;
        if (event.getPreviousServer().isEmpty()) profile = manager.createProfile(player);
        else profile = manager.getProfile(player);
        //todo check server
        if (profile == null) return;
        if (!profile.isStaff()) return;
        Session session = sessionManager.getSession(profile.getSsId());
        if (session == null) return;
        SSPlayer sus = session.getSuspect();

        Config config = this.config.get();
        Config.Buttons buttons = config.getButtons();

        Config.Ban ban = config.getBan();



        for (Config.Buttons.Button button : buttons.getButtons()) {
            player.sendMessage(button.getText()
                    .hoverEvent(HoverEvent.showText(button.getHover()))
                    .clickEvent(ClickEvent.callback(audience -> {
                        String command = ban.getCommand()
                                .replace("%player%", sus.getPlayer().getUsername())
                                .replace("%uuid%", sus.getUuid().toString())
                                .replace("%duration%", button.getDuration());
                        boolean staff = ban.getWho().isStaff();
                        server.getCommandManager().executeImmediatelyAsync(staff ? player : server.getConsoleCommandSource(), command);
                    })));
        }


        Config.Buttons.Button button = buttons.getClear();
        player.sendMessage(button.getText()
                .hoverEvent(HoverEvent.showText(button.getHover()))
                .clickEvent(ClickEvent.callback(audience -> server.getCommandManager().executeImmediatelyAsync(player, "clear " + sus.getPlayer().getUsername()))));

    }

    @Subscribe
    public void onPlayerQuit(DisconnectEvent event) {
        manager.removeProfile(event.getPlayer());
    }
}
