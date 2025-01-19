package dev.kirby.screenshare.listener;

import dev.kirby.netty.event.PacketSubscriber;
import dev.kirby.screenshare.KirbySS;
import dev.kirby.screenshare.packet.StatePacket;
import dev.kirby.screenshare.player.ScreenShareManager;
import dev.kirby.screenshare.player.ScreenSharePlayer;

public record ScreenShareEvents(KirbySS plugin, ScreenShareManager manager) {

    @PacketSubscriber
    public void onPacket(final StatePacket packet) {
        ScreenSharePlayer player = manager.getProfile(packet.getPlayer());
        if (player == null) return;
        player.setPlayerState(packet.getState());
        player.setSsId(packet.getSsId());
    }


}