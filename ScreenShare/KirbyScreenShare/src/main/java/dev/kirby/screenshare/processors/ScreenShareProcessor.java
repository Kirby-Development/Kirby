package dev.kirby.screenshare.processors;

import dev.kirby.api.player.processor.Processor;
import dev.kirby.screenshare.KirbySS;
import dev.kirby.screenshare.player.ScreenSharePlayer;
import lombok.Getter;

@Getter
public abstract class ScreenShareProcessor extends Processor<ScreenSharePlayer, KirbySS> {

    protected ScreenShareProcessor(ScreenSharePlayer player, KirbySS plugin) {
        super(player, plugin);
    }

}
