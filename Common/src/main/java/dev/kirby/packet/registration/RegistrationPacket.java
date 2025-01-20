package dev.kirby.packet.registration;

import dev.kirby.netty.Packet;
import dev.kirby.netty.buffer.PacketBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public abstract class RegistrationPacket extends Packet {

    protected String[] clientData;
    protected String[] resourceData;
    protected String licenseKey;

    @Override
    public void read(PacketBuffer buffer) {
        this.licenseKey = buffer.readUTF8();
        this.clientData = buffer.readStringCollection().toArray(new String[0]);
        this.resourceData = buffer.readStringCollection().toArray(new String[0]);
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeUTF8(licenseKey);
        buffer.writeStringCollection(Arrays.stream(clientData).toList());
        buffer.writeStringCollection(Arrays.stream(resourceData).toList());
    }

}