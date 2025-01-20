package dev.kirby.packet.empty;

import dev.kirby.netty.Packet;
import dev.kirby.netty.buffer.PacketBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PingPacket extends Packet {

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
