package dev.kirby.packet.registry;

import dev.kirby.netty.Packet;

public interface SendPacket {

        void sendPacket(final Packet packet);
    }