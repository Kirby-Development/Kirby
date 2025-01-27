package dev.kirby.screenshare.packet;

import dev.kirby.netty.Packet;
import dev.kirby.netty.buffer.PacketBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StartSession extends Packet {

    long session;

    @Override
    public void read(PacketBuffer buffer) {
        session = buffer.readLong();
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeLong(session);
    }
}
