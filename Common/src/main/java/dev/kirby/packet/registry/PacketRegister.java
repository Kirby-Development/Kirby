package dev.kirby.packet.registry;

import dev.kirby.netty.Packet;
import dev.kirby.netty.registry.IPacketRegistry;
import dev.kirby.netty.registry.SimplePacketRegistry;
import dev.kirby.packet.*;
import lombok.Getter;
import lombok.SneakyThrows;

@Getter
public class PacketRegister {

    private static PacketRegister INSTANCE;
    private final IPacketRegistry packetRegistry = new SimplePacketRegistry();

    @SafeVarargs
    @SneakyThrows
    private PacketRegister(Class<? extends Packet>... packets) {
        for (int id = 0; id < packets.length; id++) {
            packetRegistry.registerPacket(id, packets[id]);
        }
    }

    public static IPacketRegistry get() {
        if (INSTANCE == null)
            INSTANCE = new PacketRegister(LoginPacket.class,
                    Status.ResponsePacket.class,
                    PingPacket.class,
                    TextPacket.class);
        return INSTANCE.getPacketRegistry();
    }
}
