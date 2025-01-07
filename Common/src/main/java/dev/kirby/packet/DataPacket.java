package dev.kirby.packet;

import dev.kirby.netty.Packet;
import dev.kirby.netty.buffer.PacketBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DataPacket extends Packet {

    private String[] data;

    @Override
    public void read(PacketBuffer buffer) {
        this.data = buffer.readStringCollection().toArray(new String[0]);
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeStringCollection(Arrays.stream(data).toList());
    }

}