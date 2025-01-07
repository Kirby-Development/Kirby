package dev.kirby.packet;

import dev.kirby.netty.Packet;
import dev.kirby.netty.buffer.PacketBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LicensePacket extends Packet {
    private String license;
    private UUID uuid;

    @Override
    public void read(PacketBuffer buffer) {
        this.license = buffer.readUTF8();
        this.uuid = buffer.readUUID();
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeUTF8(license);
        buffer.writeUUID(uuid);
    }
}
