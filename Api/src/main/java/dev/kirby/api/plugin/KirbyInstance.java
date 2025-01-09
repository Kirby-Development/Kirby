package dev.kirby.api.plugin;

import dev.kirby.api.KirbyApi;
import dev.kirby.api.service.ServiceHelper;
import dev.kirby.api.util.KirbyLogger;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class KirbyInstance<T extends KirbyPlugin> extends JavaPlugin implements ServiceHelper {

    private final KirbyLogger test = new KirbyLogger("Test");

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
        plugin().init();
    }

    @Override
    public void onEnable() {
        test.info("Enable:", shutdown.get());
        if (shutdown.get()) return;
        test.info("Enabling plugin...", shutdown.get());
        plugin().getClient().connect();
        plugin().enable();
    }

    @Override
    public void onDisable() {
        test.info("Disable:", shutdown.get());
        if (shutdown.get()) return;
        test.info("Disabling plugin...", shutdown.get());
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
