package dev.kirby.screenshare.netty;

import dev.kirby.general.GeneralNettyServer;
import dev.kirby.netty.registry.IPacketRegistry;
import io.netty.channel.Channel;

import java.util.function.Consumer;

public class ScreenShareServer extends GeneralNettyServer {

    public ScreenShareServer(IPacketRegistry packetRegistry, Consumer<Channel> connect) {
        super(packetRegistry, connect);
    }
}
