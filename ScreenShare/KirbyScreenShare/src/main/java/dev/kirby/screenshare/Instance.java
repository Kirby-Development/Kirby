package dev.kirby.screenshare;

import dev.kirby.api.packet.PacketRegistry;
import dev.kirby.api.plugin.KirbyInstance;

public final class Instance extends KirbyInstance<KirbySS> implements PacketRegistry {

    private final KirbySS kirbySS = new KirbySS(this);

    @Override
    protected KirbySS init() {
        return kirbySS;
    }

}
