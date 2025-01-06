package dev.kirby.packet;

import dev.kirby.netty.Packet;
import dev.kirby.netty.buffer.PacketBuffer;
import lombok.Getter;

import java.util.UUID;


@Getter
public class LicensePacket extends Packet {
    private String license;
    private UUID uuid;

    public LicensePacket() {}

    public LicensePacket(String license, UUID uuid) {
        this.license = license;
        this.uuid = uuid;
    }

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
