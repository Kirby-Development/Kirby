package dev.kirby.packet.registry;

import dev.kirby.netty.Packet;
import dev.kirby.netty.registry.IPacketRegistry;
import dev.kirby.netty.registry.SimplePacketRegistry;
import dev.kirby.packet.empty.ConnectPacket;
import dev.kirby.packet.empty.PingPacket;
import dev.kirby.packet.registration.LoginPacket;
import dev.kirby.packet.registration.Status;
import dev.kirby.packet.text.TextPacket;
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
                    ConnectPacket.class,
                    PingPacket.class,
                    TextPacket.class);
        return INSTANCE.getPacketRegistry();
    }
}
