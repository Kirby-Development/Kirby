package dev.kirby.api.player;

import com.github.retrooper.packetevents.protocol.player.User;
import org.bukkit.entity.Player;

public interface KirbyUser {
    Player getPlayer();
    User getUser();
}
