package dev.kirby.screenshare;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginDescription;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.kirby.KirbyResource;
import dev.kirby.config.ConfigManager;
import dev.kirby.packet.empty.ConnectPacket;
import dev.kirby.screenshare.commands.ClearCommand;
import dev.kirby.screenshare.commands.ScreenShareCommand;
import dev.kirby.screenshare.configuration.Config;
import dev.kirby.screenshare.listener.PlayerListener;
import dev.kirby.screenshare.netty.ServerSS;
import dev.kirby.screenshare.packet.registry.Registry;
import dev.kirby.screenshare.player.SSManager;
import dev.kirby.screenshare.player.Session;
import dev.kirby.screenshare.utils.VelocityService;
import dev.kirby.service.ServiceManager;
import dev.kirby.service.ServiceRegistry;
import lombok.Getter;
import lombok.SneakyThrows;
import org.slf4j.Logger;

import java.io.File;

@Plugin(
        id = "kirbyvelocity",
        name = "KirbyVelocity",
        version = "1.0",
        authors = {"SweetyDreams_"}
)
@Getter
public class KirbyVelocity extends KirbyResource implements VelocityService {

    public static final ServiceManager MANAGER = new ServiceManager();

    @Inject
    private final Logger logger;
    @Inject
    private final ProxyServer proxy;

    private final Plugin plugin;
    private final File dataFolder;

    private final ConfigManager<Config> configManager;

    @SneakyThrows
    @Inject
    public KirbyVelocity(ProxyServer proxy, PluginDescription description, Logger logger) {
        super("KirbyVelocity", "1.0");
        plugin = getClass().getAnnotation(Plugin.class);
        this.logger = logger;
        this.proxy = proxy;

        File serverJar = new File(Plugin.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        dataFolder = new File(serverJar.getParentFile() + "/plugins/" + description.getId());


        configManager = new ConfigManager<>(dataFolder, new Config());
        this.configManager.load();

        licenseClient.setInfo(this);
        licenseClient.connect();

        //serverSS.getEventRegistry().registerEvents(serverEvents);
        serverSS.bind(6990);
    }

    private final SSManager manager = new SSManager();
    private final Session.Manager sessionManager = new Session.Manager();

    private final ServerSS serverSS = new ServerSS(Registry.get(), channel -> channel.writeAndFlush(new ConnectPacket()), manager());
    //private final ServerEvents serverEvents = new ServerEvents();

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        if (configManager == null) {
            logger.warn("Failed to load config.yml. Shutting down.");
            this.proxy.shutdown();
            return;
        }

        proxy.getEventManager().register(this, new PlayerListener(proxy, configManager, manager, sessionManager));

        CommandManager commandManager = proxy.getCommandManager();
        commandManager.register(commandManager.metaBuilder("ss").plugin(this).build(), new ScreenShareCommand(sessionManager, configManager, manager, proxy));
        commandManager.register(commandManager.metaBuilder("clear").plugin(this).build(), new ClearCommand(sessionManager, configManager, manager, proxy));
    }

    @Override
    public ServiceRegistry manager() {
        return MANAGER;
    }

    @Override
    public void destroy() {

    }


}
