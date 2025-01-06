package dev.kirby.api.netty;

import dev.kirby.netty.event.EventRegistry;
import dev.kirby.netty.exception.PacketRegistrationException;
import dev.kirby.netty.registry.IPacketRegistry;
import dev.kirby.netty.registry.SimplePacketRegistry;
import dev.kirby.packet.*;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.function.Supplier;

public class NetClientLauncher {

    public static void main(String[] args) throws PacketRegistrationException {
        IPacketRegistry packetRegistry = new SimplePacketRegistry();
        packetRegistry.registerPacket(0, ConnectPacket.class);
        packetRegistry.registerPacket(1, DataPacket.class);
        packetRegistry.registerPacket(2, Status.Packet.class);
        packetRegistry.registerPacket(3, TextPacket.class);
        packetRegistry.registerPacket(4, LicensePacket.class);

        Object[] data = {
                System.getProperty("sun.arch.data.model"),
                Runtime.getRuntime().availableProcessors(),
                System.getenv("PROCESSOR_IDENTIFIER"),
                System.getenv("PROCESSOR_ARCHITECTURE"),
                System.getenv("PROCESSOR_ARCHITEW6432"),
                System.getenv("NUMBER_OF_PROCESSORS"),
                System.getenv("PROCESSOR_LEVEL"),
                System.getenv("PROCESSOR_REVISION"),
                ((Supplier<String>) () -> {
                    try {
                        for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                            byte[] mac = networkInterface.getHardwareAddress();
                            if (mac != null) {
                                StringBuilder sb = new StringBuilder();
                                for (byte b : mac) sb.append(String.format("%02X", b));
                                return sb.toString();
                            }
                        }
                    } catch (Exception ignored) {
                    }
                    return "UNKNOWN";
                }).get()
        };

        EventRegistry eventRegistry = new EventRegistry();
        NettyClient client = new NettyClient(packetRegistry, future -> System.out.println("Client running"), eventRegistry);

        eventRegistry.registerEvents(new ClientEvents(data));

    }

}
