package dev.kirby;

import dev.kirby.api.packet.PacketRegistry;
import dev.kirby.api.plugin.KirbyInstance;

public final class Instance extends KirbyInstance<KirbyBounty> implements PacketRegistry {

    private final KirbyBounty kirbyBounty = new KirbyBounty(this);


    @Override
    protected KirbyBounty init() {
        return kirbyBounty;
    }

}
