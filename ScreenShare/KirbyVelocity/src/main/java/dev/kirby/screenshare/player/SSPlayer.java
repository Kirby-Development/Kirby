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
    //private final User user;

    private PlayerState playerState = PlayerState.NONE;
    private Integer ssId = -1;

    public SSPlayer(Player player
            //, User user
    ) {
        this.uuid = player.getUniqueId();
        this.player = player;
        //this.user = user;
    }

    public boolean isStaff() {
        boolean staff = playerState.isStaff() || player.hasPermission("kirby.screenshare.staff");
        System.out.println(player.getUsername()+" staff: "+staff);
        return staff;
    }
}
