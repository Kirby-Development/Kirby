package dev.kirby.processors;

import dev.kirby.KirbyBounty;
import dev.kirby.api.player.processor.Processor;
import dev.kirby.player.BountyPlayer;
import lombok.Getter;

@Getter
public abstract class BountyProcessor extends Processor<BountyPlayer, KirbyBounty> {

    protected BountyProcessor(BountyPlayer player, KirbyBounty plugin) {
        super(player, plugin);
    }

}
