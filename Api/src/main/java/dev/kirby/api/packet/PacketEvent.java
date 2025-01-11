package dev.kirby.api.packet;

import com.github.retrooper.packetevents.PacketEvents;
import dev.kirby.api.plugin.KirbyInstance;
import dev.kirby.api.plugin.KirbyPlugin;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;

public interface PacketEvent {

    default void load() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(getInstance()));
        PacketEvents.getAPI().load();
    }

    default void initialize() {
        PacketEvents.getAPI().init();
    }

    default void terminate() {
        PacketEvents.getAPI().terminate();
    }

    KirbyInstance<? extends KirbyPlugin> getInstance();
}