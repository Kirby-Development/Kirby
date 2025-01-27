package dev.kirby.api.plugin;

import dev.kirby.api.packet.PacketRegistry;
import dev.kirby.api.papi.PapiHook;
import dev.kirby.exception.InvalidException;
import dev.kirby.utils.Destroyable;
import dev.kirby.utils.KirbyLogo;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class KirbyInstance<T extends KirbyPlugin> extends JavaPlugin implements Destroyable {

    private T plugin = init();

    protected abstract T init();

    public T plugin() {
        if (plugin == null) plugin = init();
        if (plugin.getLicenseClient() == null || !plugin.getLicenseClient().isConnected())
            throw new InvalidException(InvalidException.Type.CONNECTION);
        return plugin;
    }

    public AtomicBoolean destroyed = new AtomicBoolean(false);

    @Override
    public void onLoad() {
        if (destroyed.get()) return;
        if (this instanceof PacketRegistry packetEvent) packetEvent.load();
        plugin().init();

        PapiHook papi = plugin().getPapi();

        if (papi.canRegister()) papi.register();
        if (papi.isInitialized()) {
            plugin().getLogger().info("Papi successfully registered.");
        }
    }

    @Override
    public void onEnable() {
        if (destroyed.get()) return;
        plugin().enable();
        if (this instanceof PacketRegistry packetEvent) packetEvent.initialize();

        plugin.getLogger().info(KirbyLogo.get(getName()));
    }

    @Override
    public void onDisable() {
        if (destroyed.get()) return;
        if (this instanceof PacketRegistry packetEvent) packetEvent.terminate();
        HandlerList.unregisterAll(this);
        Bukkit.getScheduler().cancelTasks(this);
        PapiHook papi = plugin().getPapi();
        if (papi.isInitialized()) papi.unregister();
        plugin().shutdown();
    }

    public void destroy() {
        onDisable();
        destroyed.set(true);
    }
}
