package dev.kirby.keeby;

import dev.kirby.api.plugin.KirbyInstance;

public final class Instance extends KirbyInstance<Keeby> {

    private final Keeby keeby = new Keeby(this);

    @Override
    protected Keeby load() {
        return keeby;
    }
}
