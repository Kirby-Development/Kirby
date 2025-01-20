package dev.kirby.screenshare.listener;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.event.UserLoginEvent;
import com.github.retrooper.packetevents.protocol.player.User;
import dev.kirby.api.packet.Packet;
import dev.kirby.api.packet.PacketListener;
import dev.kirby.api.util.ApiService;
import dev.kirby.screenshare.player.ScreenSharePlayer;
import dev.kirby.screenshare.player.ScreenShareManager;
import org.bukkit.entity.Player;

public class PlayerListener implements PacketListener, ApiService {

    private final ScreenShareManager manager;
    public PlayerListener() {
        manager = get(ScreenShareManager.class);
    }

    @Override
    public void onUserLogin(UserLoginEvent e) {
        if (!(e.getPlayer() instanceof Player p)) return;
        User user = e.getUser();
        manager.createProfile(p, user);
    }

    @Override
    public void handleReceive(PacketReceiveEvent e, Player p) {
        final ScreenSharePlayer player = manager.getProfile(p);
        if(player == null) return;
        player.handle(new Packet(e));
    }

    @Override
    public void handleSend(PacketSendEvent e, Player p) {
        final ScreenSharePlayer player = manager.getProfile(p);
        if(player == null) return;
        player.handle(new Packet(e));
    }
}
