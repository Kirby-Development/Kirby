package dev.kirby.api.plugin;

import dev.kirby.utils.Destroyable;
import dev.kirby.utils.InvalidException;
import dev.kirby.utils.KirbyLogger;
import org.apache.logging.log4j.Level;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class KirbyInstance<T extends KirbyPlugin> extends JavaPlugin implements Destroyable {

    private final KirbyLogger test = new KirbyLogger("Test") {

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
        if (plugin.getClient() == null || !plugin.getClient().isConnected())
            throw new InvalidException(InvalidException.Type.CONNECTION);
        return plugin;
    }

    public AtomicBoolean destroyed = new AtomicBoolean(false);


    @Override
    public void onLoad() {
        if (destroyed.get()) return;
        test.info("Loading plugin...");
        //if (this instanceof PacketEvent packetEvent) packetEvent.initialize();
        plugin().init();
    }

    @Override
    public void onEnable() {
        if (destroyed.get()) return;
        test.info("Enabling plugin...");
        plugin().enable();
        //if (this instanceof PacketEvent packetEvent) packetEvent.enable();
    }

    @Override
    public void onDisable() {
        if (destroyed.get()) return;
        test.info("Disabling plugin...");
        //if (this instanceof PacketEvent packetEvent) packetEvent.terminate();
        HandlerList.unregisterAll(this);
        Bukkit.getScheduler().cancelTasks(this);
        plugin().shutdown();
    }

    public void destroy() {
        onDisable();
        destroyed.set(true);
    }
}
