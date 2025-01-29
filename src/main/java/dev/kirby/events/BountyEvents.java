package dev.kirby.events;

import dev.kirby.KirbyBounty;
import dev.kirby.api.events.EventListener;
import dev.kirby.player.BountyManager;
import dev.kirby.player.BountyPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

public class BountyEvents implements EventListener {

    private final KirbyBounty kirbyBounty;
    private final BountyManager manager;

    public BountyEvents(KirbyBounty kirbyBounty) {
        this.kirbyBounty = kirbyBounty;
        manager = kirbyBounty.getBountyManager();
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getPlayer();
        Player killer = p.getKiller();
        if (killer == null) return;
        BountyPlayer profile = manager.getProfile(p);
        if (profile == null) return;
        BountyPlayer killerP = manager.getProfile(killer);
        if (killerP == null) return;




    }


    public void register() {
        register(kirbyBounty.getInstance());
    }
}
