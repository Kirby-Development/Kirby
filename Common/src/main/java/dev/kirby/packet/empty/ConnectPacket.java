package dev.kirby.packet.empty;

import dev.kirby.netty.Packet;
import dev.kirby.netty.buffer.PacketBuffer;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ConnectPacket extends Packet {

    @Override
    public void read(PacketBuffer buffer) {
    }

    @Override
    public void write(PacketBuffer buffer) {

    }
}
