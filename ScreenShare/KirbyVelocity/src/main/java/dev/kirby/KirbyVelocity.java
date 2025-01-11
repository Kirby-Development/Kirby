package dev.kirby;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.kirby.netty.ScreenShareServer;
import dev.kirby.netty.ServerEvents;
import dev.kirby.packet.Registry;
import lombok.Getter;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Plugin(
    id = "kirbyvelocity",
    name = "KirbyVelocity",
    version = "1.0"
    ,authors = {"SweetyDreams_"}
)
@Getter
public class KirbyVelocity {

    @Inject private Logger logger;
    @Inject private ProxyServer server;

    private final Map<UUID, PlayerState> playerStates = new HashMap<>();

    private final ScreenShareServer serverSS = new ScreenShareServer(Registry.get(), future -> logger.info("server running"), channel -> {

    });

    private final ServerEvents serverEvents = new ServerEvents(this, playerStates);

    public KirbyVelocity(Logger logger, ProxyServer server) {
        this.logger = logger;
        this.server = server;

        serverSS.getEventRegistry().registerEvents(serverEvents);
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {

    }
}
