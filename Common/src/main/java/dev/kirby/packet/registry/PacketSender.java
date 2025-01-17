package dev.kirby.packet.registry;

import dev.kirby.netty.Packet;

public interface PacketSender {
        void sendPacket(Packet packet);
    }