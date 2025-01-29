package dev.kirby.player;

import com.github.retrooper.packetevents.protocol.player.User;
import dev.kirby.KirbyBounty;
import dev.kirby.api.player.KirbyManager;
import org.bukkit.entity.Player;

public class BountyManager extends KirbyManager<BountyPlayer, KirbyBounty> {

    public BountyManager(KirbyBounty plugin) {
        super(plugin);
    }

    @Override
    protected BountyPlayer getData(Player player, User user) {
        return new BountyPlayer(player, user, plugin);
    }
}