package dev.kirby.server;

import dev.kirby.general.GeneralNettyServer;
import dev.kirby.netty.registry.IPacketRegistry;
import io.netty.channel.Channel;
import lombok.Getter;

import java.util.function.Consumer;


@Getter
public class NettyServer extends GeneralNettyServer {

    public NettyServer(IPacketRegistry packetRegistry, Consumer<Channel> connect) {
        super(packetRegistry, connect);
    }
}