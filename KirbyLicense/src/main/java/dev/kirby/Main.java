package dev.kirby;

import dev.kirby.netty.exception.PacketRegistrationException;
import dev.kirby.netty.registry.IPacketRegistry;
import dev.kirby.netty.registry.SimplePacketRegistry;
import dev.kirby.packet.ConnectPacket;
import dev.kirby.packet.DataPacket;
import dev.kirby.packet.Status;
import dev.kirby.packet.TextPacket;
import dev.kirby.server.NettyServer;

public class Main {
    public static void main(String[] args) throws PacketRegistrationException {
        IPacketRegistry packetRegistry = new SimplePacketRegistry();
        packetRegistry.registerPacket(0, ConnectPacket.class);
        packetRegistry.registerPacket(1, DataPacket.class);
        packetRegistry.registerPacket(2, Status.Packet.class);
        packetRegistry.registerPacket(3, TextPacket.class);

        new NettyServer(packetRegistry, future -> System.out.println("Server running"));





    }

}