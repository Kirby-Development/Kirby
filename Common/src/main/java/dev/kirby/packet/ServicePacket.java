package dev.kirby.packet;

import dev.kirby.netty.Packet;
import dev.kirby.netty.buffer.PacketBuffer;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class ServicePacket extends Packet {

    @Override
    public void read(PacketBuffer buffer) {

    }

    @Override
    public void write(PacketBuffer buffer) {

    }
}
