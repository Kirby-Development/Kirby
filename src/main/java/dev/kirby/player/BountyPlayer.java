package dev.kirby.player;

import com.github.retrooper.packetevents.protocol.player.User;
import dev.kirby.KirbyBounty;
import dev.kirby.api.packet.Packet;
import dev.kirby.api.packet.PacketHandler;
import dev.kirby.api.player.KirbyUser;
import dev.kirby.api.util.ApiService;
import dev.kirby.processors.BountyProcessor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class BountyPlayer implements KirbyUser, PacketHandler, ApiService {

    private final UUID uuid;
    private final Player player;
    private final User user;

    private double bounty = 0;

    public BountyPlayer(Player player, User user, KirbyBounty plugin) {
        this.uuid = player.getUniqueId();
        this.player = player;
        this.user = user;
    }

    public void increment(double amount) {
        bounty += amount;
    }

    private final List<BountyProcessor> processors = new ArrayList<>();


    @Override
    public void handle(Packet packet) {
        processors.parallelStream().forEach(packet::handle);
    }

}
