package dev.kirby.screenshare.player;

import com.github.retrooper.packetevents.protocol.player.User;
import dev.kirby.api.player.KirbyManager;
import dev.kirby.api.plugin.KirbyInstance;
import dev.kirby.api.plugin.KirbyPlugin;
import org.bukkit.entity.Player;

public class ScreenShareManager extends KirbyManager<ScreenSharePlayer> {

    public ScreenShareManager(KirbyInstance<? extends KirbyPlugin> instance) {
        super(instance);
    }

    @Override
    protected ScreenSharePlayer getData(Player player, User user) {
        return new ScreenSharePlayer(player, user, instance);
    }
}
