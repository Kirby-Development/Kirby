package dev.kirby;

import com.github.retrooper.packetevents.PacketEvents;
import dev.kirby.api.packet.PacketRegistry;
import dev.kirby.api.plugin.KirbyInstance;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;

public final class Instance extends KirbyInstance<Kirby> implements PacketRegistry {

    private final Kirby kirby = new Kirby(this);


    @Override
    protected Kirby init() {
        return kirby;
    }

}
