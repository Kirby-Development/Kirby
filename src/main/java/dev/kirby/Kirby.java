package dev.kirby;

import dev.kirby.api.plugin.KirbyInstance;
import dev.kirby.api.plugin.KirbyPlugin;

public class Kirby extends KirbyPlugin {

    public Kirby(KirbyInstance<? extends KirbyPlugin> plugin) {
        super(plugin);
    }

    @Override
    public void init() {
        getLogger().info("Kirby init");
    }

    @Override
    public void enable() {
        getLogger().info("Kirby enable");
    }

    @Override
    public void shutdown() {
        getLogger().info("Kirby shutdown");
    }
}
