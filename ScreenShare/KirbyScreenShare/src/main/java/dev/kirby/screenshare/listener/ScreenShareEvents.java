package dev.kirby.screenshare.listener;

import dev.kirby.netty.event.PacketSubscriber;
import dev.kirby.screenshare.KirbySS;
import dev.kirby.screenshare.packet.EndSession;
import dev.kirby.screenshare.packet.StartSession;
import dev.kirby.screenshare.packet.StatePacket;
import dev.kirby.screenshare.player.ScreenShareManager;
import dev.kirby.screenshare.player.ScreenSharePlayer;

import java.util.Map;

public record ScreenShareEvents(KirbySS plugin, ScreenShareManager manager, Map<Integer, Long> screenShareTime) {

    @PacketSubscriber
    public void onState(final StatePacket packet) {
        ScreenSharePlayer player = manager.getProfile(packet.getPlayer());
        if (player == null) return;
        player.setPlayerState(packet.getState());
        player.setSsId(packet.getSsId());
    }

    @PacketSubscriber
    public void onStart(final StartSession packet) {
        screenShareTime.put(packet.getSession(), System.currentTimeMillis());
    }

    @PacketSubscriber
    public void onEnd(final EndSession packet) {
        screenShareTime.remove(packet.getSession());
    }


}