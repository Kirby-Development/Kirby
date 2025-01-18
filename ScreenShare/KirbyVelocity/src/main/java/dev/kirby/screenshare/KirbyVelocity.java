package dev.kirby.screenshare;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginDescription;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.kirby.packet.ConnectPacket;
import dev.kirby.screenshare.commands.ClearCommand;
import dev.kirby.screenshare.commands.ScreenShareCommand;
import dev.kirby.screenshare.configuration.ConfigManager;
import dev.kirby.screenshare.configuration.Configuration;
import dev.kirby.screenshare.listener.PlayerListener;
import dev.kirby.screenshare.netty.ScreenShareServer;
import dev.kirby.screenshare.netty.ServerEvents;
import dev.kirby.screenshare.packet.registry.Registry;
import dev.kirby.screenshare.player.SSManager;
import dev.kirby.screenshare.player.Session;
import dev.kirby.screenshare.utils.VelocityService;
import dev.kirby.service.ServiceManager;
import lombok.Getter;
import org.slf4j.Logger;

@Plugin(
        id = "kirbyvelocity",
        name = "KirbyVelocity",
        version = "1.0",
        authors = {"SweetyDreams_"}
)
@Getter
public class KirbyVelocity implements VelocityService {

    public static final ServiceManager MANAGER = new ServiceManager();

    @Inject
    private final Logger logger;
    @Inject
    private final ProxyServer server;

    private final Configuration config;

    public Configuration getConfig() {
        return configManager.get("config.yml");
    }

    private final ConfigManager configManager;

    @Inject
    public KirbyVelocity(ProxyServer server, PluginDescription description, Logger logger) {
        this.logger = logger;
        this.server = server;
        this.configManager = new ConfigManager(description);
        this.configManager.create("config.yml");
        config = getConfig();
        if (config == null) {
            logger.error("Failed to load config.yml. Shutting down.");
            this.server.shutdown();
        }

        serverSS.getEventRegistry().registerEvents(serverEvents);
        serverSS.bind(6990);
        install(SSManager.class, manager);
        install(ProxyServer.class, server);
        install(Configuration.class, config);
    }

    private final SSManager manager = new SSManager();
    private final Session.Manager sessionManager = new Session.Manager();

    private final ScreenShareServer serverSS = new ScreenShareServer(Registry.get(), channel -> channel.writeAndFlush(new ConnectPacket()), manager());
    private final ServerEvents serverEvents = new ServerEvents(this, manager, sessionManager);

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        if (config == null) {
            logger.warn("Failed to load config.yml. Shutting down.");
            this.server.shutdown();
            return;
        }

        server.getEventManager().register(this, new PlayerListener(server,config, manager, sessionManager));

        CommandManager commandManager = server.getCommandManager();
        commandManager.register(commandManager.metaBuilder("ss").plugin(this).build(), new ScreenShareCommand(sessionManager, manager, server));
        commandManager.register(commandManager.metaBuilder("clear").plugin(this).build(), new ClearCommand(sessionManager, manager, server));
    }

}
