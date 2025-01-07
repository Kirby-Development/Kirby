package dev.kirby.packet;

import dev.kirby.netty.Packet;
import dev.kirby.netty.buffer.PacketBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TextPacket extends Packet {
    private String text;

    @Override
    public void read(PacketBuffer buffer) {
        this.text = buffer.readUTF8();
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeUTF8(text);
    }
}
