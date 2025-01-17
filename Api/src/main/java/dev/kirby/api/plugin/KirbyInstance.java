package dev.kirby.api.plugin;

import dev.kirby.api.netty.NettyClient;
import dev.kirby.api.packet.PacketEvent;
import dev.kirby.KirbyLogger;
import dev.kirby.api.util.InvalidException;
import dev.kirby.packet.registry.PacketRegister;
import org.apache.logging.log4j.Level;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class KirbyInstance<T extends KirbyPlugin> extends JavaPlugin  {

    private final KirbyLogger test = new KirbyLogger("Test"){

        public final boolean DEBUG = true;

        @Override
        public void log(Level level, Object... input) {
            if (!DEBUG) return;
            super.log(level, input);
        }
    };

    private final NettyClient client;

    private T plugin = load();

    protected abstract T load();

    public KirbyInstance(@NotNull JavaPluginLoader loader, @NotNull PluginDescriptionFile description, @NotNull File dataFolder, @NotNull File file) {
        super(loader, description, dataFolder, file);
        client = new NettyClient(PacketRegister.get(), load(), this::shutdown);
    }

    public KirbyInstance() {
        client = new NettyClient(PacketRegister.get(), load(), this::shutdown);
    }

    public T plugin() {
        if (plugin == null) plugin = load();
        if (client == null || !client.isConnected()) throw new InvalidException(InvalidException.Type.CONNECTION);
        return plugin;
    }

    public AtomicBoolean shutdown = new AtomicBoolean(false);


    @Override
    public void onLoad() {
        client.connect();
        if (shutdown.get()) return;
        test.info("Loading plugin...");
        if (plugin() instanceof PacketEvent packetEvent) packetEvent.load();
        plugin().init();
    }

    @Override
    public void onEnable() {
        if (shutdown.get()) return;
        test.info("Enabling plugin...");
        plugin().enable();
        if (plugin() instanceof PacketEvent packetEvent) packetEvent.initialize();
    }

    @Override
    public void onDisable() {
        if (shutdown.get()) return;
        test.info("Disabling plugin...");
        if (plugin() instanceof PacketEvent packetEvent) packetEvent.terminate();
        HandlerList.unregisterAll(this);
        Bukkit.getScheduler().cancelTasks(this);
        plugin().shutdown();
    }

    public void shutdown() {
        onDisable();
        shutdown.set(true);
    }
}
