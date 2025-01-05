package dev.kirby.api.plugin;

import com.github.retrooper.packetevents.PacketEvents;
import dev.kirby.api.KirbyApi;
import dev.kirby.api.service.ServiceHelper;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public abstract class KirbyInstance<T extends KirbyPlugin> extends JavaPlugin implements ServiceHelper {

    private final String name;
    private final T plugin = load();

    public KirbyInstance() {
        this.name = plugin.getClass().getSimpleName();
        KirbyApi.getRegistry().install(this);
    }

    @Override
    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();
        plugin.init();
    }

    @Override
    public void onEnable() {
        plugin.enable();
        PacketEvents.getAPI().init();
    }

    @Override
    public void onDisable() {
        PacketEvents.getAPI().terminate();
        HandlerList.unregisterAll(this);
        Bukkit.getScheduler().cancelTasks(this);
        plugin.shutdown();
    }

    protected abstract T load();

}
