package dev.kirby.api.plugin;

import dev.kirby.api.packet.PacketEvent;
import dev.kirby.KirbyLogger;
import dev.kirby.api.util.InvalidException;
import org.apache.logging.log4j.Level;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class KirbyInstance<T extends KirbyPlugin> extends JavaPlugin  {

    private final KirbyLogger test = new KirbyLogger("Test"){

        public final boolean DEBUG = false;

        @Override
        public void log(Level level, Object... input) {
            if (!DEBUG) return;
            super.log(level, input);
        }
    };

    private T plugin = load();

    protected abstract T load();

    public T plugin() {
        if (plugin == null) plugin = load();
        if (!plugin.getClient().isConnected()) throw new InvalidException(InvalidException.Type.CONNECTION);
        return plugin;
    }

    public AtomicBoolean shutdown = new AtomicBoolean(false);

    @Override
    public void onLoad() {
        test.info("Load:", shutdown.get());
        if (shutdown.get()) return;
        test.info("Loading plugin...", shutdown.get());
        if (plugin() instanceof PacketEvent packetEvent) packetEvent.load();
        plugin().init();
    }

    @Override
    public void onEnable() {
        test.info("Enable:", shutdown.get());
        if (shutdown.get()) return;
        test.info("Enabling plugin...", shutdown.get());
        //todo uncomment
        plugin().getClient().connect();
        plugin().enable();
        if (plugin() instanceof PacketEvent packetEvent) packetEvent.initialize();
    }

    @Override
    public void onDisable() {
        test.info("Disable:", shutdown.get());
        if (shutdown.get()) return;
        test.info("Disabling plugin...", shutdown.get());
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
