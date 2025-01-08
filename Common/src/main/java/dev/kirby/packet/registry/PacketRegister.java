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
        int id = 0;
        packetRegistry.registerPacket(id++, LoginPacket.class);
        packetRegistry.registerPacket(id++, Status.ResponsePacket.class);
        packetRegistry.registerPacket(id++, TextPacket.class);
    }


    public static PacketRegister get() {
        if (INSTANCE == null) INSTANCE = new PacketRegister();
        return INSTANCE;
    }
}
