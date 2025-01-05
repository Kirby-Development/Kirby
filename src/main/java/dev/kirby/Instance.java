package dev.kirby;

import dev.kirby.api.plugin.KirbyInstance;

public final class Instance extends KirbyInstance<Kirby> {

    private final Kirby kirby = new Kirby(this);

    @Override
    protected Kirby load() {
        return kirby;
    }
}
