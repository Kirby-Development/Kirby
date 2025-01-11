package dev.kirby.screenshare.player;

import com.github.retrooper.packetevents.protocol.player.User;
import dev.kirby.PlayerState;
import dev.kirby.api.packet.listener.Packet;
import dev.kirby.api.packet.listener.PacketHandler;
import dev.kirby.api.player.KirbyUser;
import dev.kirby.api.plugin.KirbyInstance;
import dev.kirby.api.plugin.KirbyPlugin;
import dev.kirby.api.util.ServiceHelper;
import dev.kirby.screenshare.processors.ChatProcessor;
import dev.kirby.screenshare.processors.ScreenShareProcessor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
public class ScreenSharePlayer implements KirbyUser, PacketHandler, ServiceHelper {

    private final Player player;
    private final User user;
    private PlayerState playerState = PlayerState.NONE;

    private final Optional<UUID> ssId = Optional.empty();

    public ScreenSharePlayer(Player player, User user, KirbyInstance<? extends KirbyPlugin> instance) {
        this.player = player;
        this.user = user;
        chatProcessor = new ChatProcessor(this, instance.plugin());
    }

    private final List<ScreenShareProcessor> processors = new ArrayList<>();
    private final ChatProcessor chatProcessor;

    @Override
    public void handle(Packet packet) {
        processors.parallelStream().forEach(packet::handle);
    }
}
