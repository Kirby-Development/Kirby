package dev.kirby.screenshare.packet.registry;

import dev.kirby.netty.Packet;
import dev.kirby.netty.registry.IPacketRegistry;
import dev.kirby.netty.registry.SimplePacketRegistry;
import dev.kirby.packet.ConnectPacket;
import dev.kirby.screenshare.packet.StatePacket;
import lombok.Getter;
import lombok.SneakyThrows;

@Getter
public class Registry {

    private static Registry INSTANCE;
    private final IPacketRegistry packetRegistry = new SimplePacketRegistry();

    @SafeVarargs
    @SneakyThrows
    private Registry(Class<? extends Packet>... packets)  {
        for (int id = 0; id < packets.length; id++) {
            packetRegistry.registerPacket(id, packets[id]);
        }
    }

    public static IPacketRegistry get() {
        if (INSTANCE == null) INSTANCE = new Registry(ConnectPacket.class, StatePacket.class);
        return INSTANCE.getPacketRegistry();
    }
}
