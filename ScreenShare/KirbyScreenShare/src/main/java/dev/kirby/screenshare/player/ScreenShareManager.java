package dev.kirby.screenshare.player;

import com.github.retrooper.packetevents.protocol.player.User;
import dev.kirby.api.player.KirbyManager;
import dev.kirby.screenshare.KirbySS;
import org.bukkit.entity.Player;

public class ScreenShareManager extends KirbyManager<ScreenSharePlayer, KirbySS> {

    public ScreenShareManager(KirbySS plugin) {
        super(plugin);
    }

    @Override
    protected ScreenSharePlayer getData(Player player, User user) {
        return new ScreenSharePlayer(player, user, plugin);
    }
}
