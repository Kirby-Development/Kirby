package dev.kirby;

import dev.kirby.api.packet.PacketRegistry;
import dev.kirby.api.plugin.KirbyInstance;

public final class Instance extends KirbyInstance<Kirby> implements PacketRegistry {

    private final Kirby kirby = new Kirby(this);


    @Override
    protected Kirby init() {
        return kirby;
    }

}
