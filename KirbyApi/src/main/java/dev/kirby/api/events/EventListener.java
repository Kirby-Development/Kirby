package dev.kirby.api.events;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public interface EventListener extends Listener {

    default void register(final Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

}
