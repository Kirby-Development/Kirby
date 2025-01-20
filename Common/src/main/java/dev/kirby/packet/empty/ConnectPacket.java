package dev.kirby.packet.empty;

import dev.kirby.netty.Packet;
import dev.kirby.netty.buffer.PacketBuffer;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ConnectPacket extends Packet {

    private long clientId = -1;

    @Override
    public void read(PacketBuffer buffer) {
        clientId = buffer.readLong();
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeLong(clientId);
    }
}
