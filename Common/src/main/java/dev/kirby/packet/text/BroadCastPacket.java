package dev.kirby.packet.text;

import dev.kirby.netty.Packet;
import dev.kirby.netty.buffer.PacketBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BroadCastPacket extends Packet {

    private String format;
    private String[] args;
    private LogLevel level;

    @Override
    public void read(PacketBuffer buffer) {
        this.format = buffer.readUTF8();
        this.args = buffer.readStringArray();
        this.level = buffer.readEnum(LogLevel.class);
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeUTF8(this.format);
        buffer.writeStringArray(this.args);
        buffer.writeEnum(this.level);
    }

    public enum LogLevel {
        INFO, WARN, ERROR, DEBUG
    }

}
