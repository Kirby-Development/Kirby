package dev.kirby.screenshare.processors;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.util.crypto.MessageSignData;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChatMessage;
import dev.kirby.api.packet.listener.Packet;
import dev.kirby.api.plugin.KirbyPlugin;
import dev.kirby.api.util.ServiceHelper;
import dev.kirby.screenshare.player.ScreenShareManager;
import dev.kirby.screenshare.player.ScreenSharePlayer;
import dev.kirby.screenshare.player.PlayerState;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

import static com.github.retrooper.packetevents.protocol.packettype.PacketType.Play.Client.CHAT_MESSAGE;


public class ChatProcessor extends ScreenShareProcessor implements ServiceHelper {

    private final ScreenShareManager manager;

    public ChatProcessor(ScreenSharePlayer player, KirbyPlugin plugin) {
        super(player, plugin);
        manager = get(ScreenShareManager.class);
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
        if (player.getSsId().isEmpty()) return;
        UUID id = player.getSsId().get();
        final Player p = player.getPlayer();
        final String message = wrap.getMessage();
        String tag = getPlugin().getConfig().getString("tags." + player.getPlayerState().name().toLowerCase());
        String format = getPlugin().getConfig().getString("format");
        String newMessage = format.replace("%tag%", tag).replace("%name%", p.getName()).replace("%message%", message);

        WrapperPlayClientChatMessage wrap1 = new WrapperPlayClientChatMessage(newMessage, data.get(), wrap.getLastSeenMessages());
        for (ScreenSharePlayer player : manager.getProfiles().values()) {
            if (player.getSsId().isEmpty()) continue;
            if (!player.getSsId().get().equals(id)) continue;
            PacketEvents.getAPI().getPlayerManager().receivePacketSilently(player.getUser(), wrap1);
        }
    }

}
