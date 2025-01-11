package dev.kirby.screenshare;

import dev.kirby.api.plugin.KirbyInstance;

public final class Instance extends KirbyInstance<KirbyScreenShare> {

    private final KirbyScreenShare kirbyScreenShare = new KirbyScreenShare(this);

    @Override
    protected KirbyScreenShare load() {
        return kirbyScreenShare;
    }
}
