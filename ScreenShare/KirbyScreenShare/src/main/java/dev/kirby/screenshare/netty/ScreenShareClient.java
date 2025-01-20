package dev.kirby.screenshare.netty;

import dev.kirby.general.GeneralNettyClient;
import dev.kirby.netty.registry.IPacketRegistry;
import dev.kirby.service.ServiceRegistry;

public class ScreenShareClient extends GeneralNettyClient {
    public ScreenShareClient(IPacketRegistry packetRegistry, Runnable shutdownHook, String loggerName, ServiceRegistry serviceRegistry) {
        super(packetRegistry, shutdownHook, loggerName, serviceRegistry);
    }
}
