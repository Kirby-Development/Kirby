package dev.kirby.api.player.processor;

import dev.kirby.api.packet.PacketHandler;
import dev.kirby.api.player.KirbyUser;
import dev.kirby.api.plugin.KirbyPlugin;
import lombok.Getter;

@Getter
public abstract class Processor<T extends KirbyUser, K extends KirbyPlugin> implements PacketHandler {

    protected final T player;
    private final K plugin;
    protected Processor(T player, K plugin) {
        this.player = player;
        this.plugin = plugin;
    }

}
