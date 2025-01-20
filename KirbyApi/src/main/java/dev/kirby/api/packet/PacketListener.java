package dev.kirby.api.packet;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import org.bukkit.entity.Player;

public interface PacketListener extends com.github.retrooper.packetevents.event.PacketListener {

    default PacketListenerPriority priority() {
        return PacketListenerPriority.LOW;
    }

    default void register() {
        PacketEvents.getAPI().getEventManager().registerListener(this, priority());
    }

    default void onPacketReceive(PacketReceiveEvent event) {
        if (!(event.getPlayer() instanceof Player p)) return;
        handleReceive(event, p);
    }

    default void onPacketSend(PacketSendEvent event) {
        if (!(event.getPlayer() instanceof Player p)) return;
        handleSend(event, p);
    }

    default void handleSend(PacketSendEvent e, Player p) {
    }
    default void handleReceive(PacketReceiveEvent e, Player p) {
    }
}
