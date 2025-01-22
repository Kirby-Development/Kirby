package dev.kirby.screenshare.netty;

import dev.kirby.general.GeneralNettyServer;
import dev.kirby.netty.registry.IPacketRegistry;
import dev.kirby.service.ServiceRegistry;
import io.netty.channel.Channel;

import java.util.function.Consumer;

public class ServerSS extends GeneralNettyServer {

    public ServerSS(IPacketRegistry packetRegistry, Consumer<Channel> connect, ServiceRegistry serviceRegistry) {
        super(packetRegistry, connect, serviceRegistry);
    }

}
