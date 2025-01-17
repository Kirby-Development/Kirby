package dev.kirby.screenshare.player;

import com.github.retrooper.packetevents.protocol.player.User;
import dev.kirby.screenshare.PlayerState;
import dev.kirby.api.packet.listener.Packet;
import dev.kirby.api.packet.listener.PacketHandler;
import dev.kirby.api.player.KirbyUser;
import dev.kirby.api.plugin.KirbyInstance;
import dev.kirby.api.plugin.KirbyPlugin;
import dev.kirby.api.util.ApiService;
import dev.kirby.screenshare.processors.ChatProcessor;
import dev.kirby.screenshare.processors.ScreenShareProcessor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ScreenSharePlayer implements KirbyUser, PacketHandler, ApiService {

    private final UUID uuid;
    private final Player player;
    private final User user;

    private PlayerState playerState = PlayerState.NONE;
    private Integer ssId = 0;

    public ScreenSharePlayer(Player player, User user, KirbyInstance<? extends KirbyPlugin> instance) {
        this.uuid = player.getUniqueId();
        this.player = player;
        this.user = user;
        chatProcessor = new ChatProcessor(this, instance.plugin());
    }

    public boolean isStaff() {
        return playerState.isStaff() || player.hasPermission("kirby.screenshare.staff");
    }

    private final List<ScreenShareProcessor> processors = new ArrayList<>();
    private final ChatProcessor chatProcessor;

    @Override
    public void handle(Packet packet) {
        processors.parallelStream().forEach(packet::handle);
    }
}
