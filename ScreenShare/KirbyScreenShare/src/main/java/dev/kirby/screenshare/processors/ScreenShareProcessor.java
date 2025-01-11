package dev.kirby.screenshare.processors;

import dev.kirby.api.player.processor.Processor;
import dev.kirby.api.plugin.KirbyPlugin;
import dev.kirby.screenshare.player.ScreenSharePlayer;
import lombok.Getter;

@Getter
public abstract class ScreenShareProcessor extends Processor<ScreenSharePlayer> {

    protected ScreenShareProcessor(ScreenSharePlayer player, KirbyPlugin plugin) {
        super(player, plugin);
    }

}
