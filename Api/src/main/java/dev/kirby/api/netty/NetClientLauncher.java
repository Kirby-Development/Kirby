package dev.kirby.api.netty;

import dev.kirby.packet.registry.PacketRegister;

public class NetClientLauncher {

    public static void main(String[] args) {
        NettyClient client = new NettyClient(PacketRegister.get().getPacketRegistry(),
                future -> System.out.println("Client running"), new String[]{"test", "1.0"}, "testkey", () -> {
            System.out.println("shutting down!");
        });
    }

}
