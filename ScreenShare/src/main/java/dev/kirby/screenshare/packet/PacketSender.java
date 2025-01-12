package dev.kirby.screenshare.packet;

import dev.kirby.netty.Packet;

public interface PacketSender {
        void sendPacket(Packet packet);
    }