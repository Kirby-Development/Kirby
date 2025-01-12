package dev.kirby.screenshare.netty;

import dev.kirby.general.GeneralNettyServer;
import dev.kirby.netty.registry.IPacketRegistry;
import io.netty.channel.Channel;

import java.util.concurrent.Future;
import java.util.function.Consumer;

public class ScreenShareServer extends GeneralNettyServer {

    public ScreenShareServer(IPacketRegistry packetRegistry, Consumer<Future<? super Void>> doneCallback, Consumer<Channel> connect) {
        super(packetRegistry, doneCallback, connect);
    }
}
