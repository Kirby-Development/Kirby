package dev.kirby.packet;

import dev.kirby.netty.Packet;
import dev.kirby.netty.buffer.PacketBuffer;
import lombok.Getter;


@Getter
public class TextPacket extends Packet {
    private String text;

    public TextPacket() {}

    public TextPacket(String text) {
        this.text = text;
    }

    @Override
    public void read(PacketBuffer buffer) {
        this.text = buffer.readUTF8();
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeUTF8(text);
    }
}
