package dev.kirby;

import dev.kirby.api.packet.PacketEvent;
import dev.kirby.api.plugin.KirbyInstance;
import dev.kirby.api.plugin.KirbyPlugin;
import dev.kirby.api.util.ServiceHelper;

public class Kirby extends KirbyPlugin implements PacketEvent, ServiceHelper {

    public Kirby(KirbyInstance<? extends KirbyPlugin> plugin) {
        super(plugin);
        install(PacketEvent.class, this);
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
