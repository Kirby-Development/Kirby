package dev.kirby.api.netty;

import dev.kirby.Utils;
import dev.kirby.packet.TextPacket;
import dev.kirby.packet.registry.PacketRegister;

public class NetClientLauncher {

    public static void main(String[] args) throws InterruptedException {

        //todo move in mc
        NettyClient client = new NettyClient(PacketRegister.get().getPacketRegistry(), future -> System.out.println("Client running"), new ClientEvents(Utils.getData()));



        Thread.sleep(1000);

        client.sendPacket(ctx -> new TextPacket("Helo!"));
    }

}
