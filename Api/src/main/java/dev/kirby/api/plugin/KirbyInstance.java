package dev.kirby.api.plugin;

import com.github.retrooper.packetevents.PacketEvents;
import dev.kirby.api.KirbyApi;
import dev.kirby.api.service.ServiceHelper;
import dev.kirby.api.util.KirbyLogger;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.apache.logging.log4j.Level;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class KirbyInstance<T extends KirbyPlugin> extends JavaPlugin implements ServiceHelper {

    private final KirbyLogger test = new KirbyLogger("Test"){

        public final boolean debug = false;

        @Override
        public void log(Level level, Object... input) {
            if (!debug) return;
            super.log(level, input);
        }
    };

    private T plugin = load();

    protected abstract T load();

    public T plugin() {
        if (plugin == null) plugin = load();
        return plugin;
    }

    public AtomicBoolean shutdown = new AtomicBoolean(false);

    public KirbyInstance() {
        KirbyApi.getRegistry().install(this);
    }

    @Override
    public void onLoad() {
        test.info("Load:", shutdown.get());
        if (shutdown.get()) return;
        test.info("Loading plugin...", shutdown.get());
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();
        plugin().init();
    }

    @Override
    public void onEnable() {
        test.info("Enable:", shutdown.get());
        if (shutdown.get()) return;
        test.info("Enabling plugin...", shutdown.get());
        plugin().getClient().connect();
        plugin().enable();
        PacketEvents.getAPI().init();
    }

    @Override
    public void onDisable() {
        test.info("Disable:", shutdown.get());
        if (shutdown.get()) return;
        test.info("Disabling plugin...", shutdown.get());
        PacketEvents.getAPI().terminate();
        HandlerList.unregisterAll(this);
        Bukkit.getScheduler().cancelTasks(this);
        KirbyApi.getRegistry().unregister(this);
        KirbyApi.getRegistry().unregister(plugin());
        plugin().shutdown();
    }

    public void shutdown() {
        onDisable();
        shutdown.set(true);
    }
}
