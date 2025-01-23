package dev.kirby.api.packet;

import com.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.plugin.Plugin;

public interface PacketRegistry extends Plugin {

    default void load() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();
    }

    default void initialize() {
        PacketEvents.getAPI().init();
    }

    default void terminate() {
        PacketEvents.getAPI().terminate();
    }


}
