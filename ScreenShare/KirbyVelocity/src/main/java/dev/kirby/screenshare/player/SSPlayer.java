package dev.kirby.screenshare.player;

import com.velocitypowered.api.proxy.Player;
import dev.kirby.screenshare.PlayerState;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class SSPlayer {

    private final UUID uuid;
    private final Player player;

    private PlayerState playerState = PlayerState.NONE;
    private Integer ssId = -1;

    public SSPlayer(Player player) {
        this.uuid = player.getUniqueId();
        this.player = player;
    }

    public boolean isStaff() {
        boolean staff = playerState.isStaff() || player.hasPermission("kirby.screenshare.staff");
        System.out.println(player.getUsername()+" staff: "+staff);
        return staff;
    }
}
