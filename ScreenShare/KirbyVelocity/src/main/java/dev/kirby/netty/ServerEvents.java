package dev.kirby.netty;

import dev.kirby.KirbyVelocity;
import dev.kirby.PlayerState;
import dev.kirby.netty.event.PacketSubscriber;
import dev.kirby.packet.StatePacket;

import java.util.Map;
import java.util.UUID;

public record ServerEvents(KirbyVelocity kirbyVelocity, Map<UUID, PlayerState> playerStates) {

    @PacketSubscriber
    public void onPacket(final StatePacket packet) {
        playerStates.put(packet.getPlayer(), packet.getState());
    }

}
