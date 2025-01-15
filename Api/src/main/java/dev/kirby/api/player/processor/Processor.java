package dev.kirby.api.player.processor;

import dev.kirby.api.packet.listener.PacketHandler;
import dev.kirby.api.player.KirbyUser;
import dev.kirby.api.plugin.KirbyPlugin;
import lombok.Getter;

@Getter
public abstract class Processor<T extends KirbyUser> implements PacketHandler {

    protected final T player;
    private final KirbyPlugin plugin;
    protected Processor(T player, KirbyPlugin plugin) {
        this.player = player;
        this.plugin = plugin;
    }

}
