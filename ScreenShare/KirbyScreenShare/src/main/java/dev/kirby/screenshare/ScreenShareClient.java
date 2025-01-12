package dev.kirby.screenshare;

import dev.kirby.general.GeneralNettyClient;
import dev.kirby.netty.registry.IPacketRegistry;

public class ScreenShareClient extends GeneralNettyClient {
    public ScreenShareClient(IPacketRegistry packetRegistry, Runnable shutdownHook, String loggerName) {
        super(packetRegistry, shutdownHook, loggerName);
    }
}
