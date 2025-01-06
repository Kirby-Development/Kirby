package dev.kirby.packet;

import dev.kirby.netty.Packet;
import dev.kirby.netty.buffer.PacketBuffer;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DataPacket extends Packet {

    private String data;

    public DataPacket(Object[] data) {
        StringBuilder builder = new StringBuilder();
        for (Object o : data) builder.append(o).append("|");
        this.data = builder.toString();
    }

    public DataPacket(String data) {
        this.data = data;
    }

    @Override
    public void read(PacketBuffer buffer) {
        this.data = buffer.readUTF8();
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeUTF8(data);
    }

    public Object[] getData() {
        return data.split("\\|");
    }
}