package dev.kirby.packet.registry;

import dev.kirby.netty.registry.IPacketRegistry;
import dev.kirby.netty.registry.SimplePacketRegistry;
import dev.kirby.packet.*;
import lombok.Getter;
import lombok.SneakyThrows;

@Getter
public class PacketRegister {

    private static PacketRegister INSTANCE = new PacketRegister();
    private final IPacketRegistry packetRegistry = new SimplePacketRegistry();

    @SneakyThrows
    private PacketRegister()  {
        packetRegistry.registerPacket(0, ConnectPacket.class);
        packetRegistry.registerPacket(1, DataPacket.class);
        packetRegistry.registerPacket(2, Status.Packet.class);
        packetRegistry.registerPacket(3, TextPacket.class);
        packetRegistry.registerPacket(4, LicensePacket.class);
    }


    public static PacketRegister get() {
        if (INSTANCE == null) INSTANCE = new PacketRegister();
        return INSTANCE;
    }
}
