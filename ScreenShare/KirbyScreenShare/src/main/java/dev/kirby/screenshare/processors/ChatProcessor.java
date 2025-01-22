package dev.kirby.screenshare.processors;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.util.crypto.MessageSignData;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChatMessage;
import dev.kirby.api.packet.Packet;
import dev.kirby.api.util.ApiService;
import dev.kirby.screenshare.KirbySS;
import dev.kirby.screenshare.PlayerState;
import dev.kirby.screenshare.config.Config;
import dev.kirby.screenshare.player.ScreenShareManager;
import dev.kirby.screenshare.player.ScreenSharePlayer;
import org.bukkit.entity.Player;

import java.util.Optional;

import static com.github.retrooper.packetevents.protocol.packettype.PacketType.Play.Client.CHAT_MESSAGE;


public class ChatProcessor extends ScreenShareProcessor implements ApiService {

    private final ScreenShareManager manager;

    public ChatProcessor(ScreenSharePlayer player, KirbySS plugin) {
        super(player, plugin);
        manager = plugin.getManager();
    }

    @Override
    public void handle(Packet packet) {
        if (packet.server()) return;
        final PacketReceiveEvent e = packet.receive();
        if (!packet.type().equals(CHAT_MESSAGE)) return;
        e.setCancelled(true);
        final WrapperPlayClientChatMessage wrap = new WrapperPlayClientChatMessage(e);
        final Optional<MessageSignData> data = wrap.getMessageSignData();
        if (data.isEmpty()) return;
        if (player.getPlayerState().equals(PlayerState.NONE)) return;
        if (player.getSsId() == 0) return;
        int id = player.getSsId();
        final Player p = player.getPlayer();
        final String message = wrap.getMessage();

        Config config = getPlugin().getConfigManager().get();

        String tag = config.getTag(player.getPlayerState());
        String format = config.getFormat();
        String newMessage = format.replace("%tag%", tag).replace("%name%", p.getName()).replace("%message%", message);

        WrapperPlayClientChatMessage wrap1 = new WrapperPlayClientChatMessage(newMessage, data.get(), wrap.getLastSeenMessages());
        for (ScreenSharePlayer player : manager.getProfiles().values()) {
            if (!player.isStaff()) {
                if (player.getSsId() == 0) continue;
                if (!player.getSsId().equals(id)) continue;
            }
            PacketEvents.getAPI().getPlayerManager().receivePacketSilently(player.getUser(), wrap1);
        }
    }

}
