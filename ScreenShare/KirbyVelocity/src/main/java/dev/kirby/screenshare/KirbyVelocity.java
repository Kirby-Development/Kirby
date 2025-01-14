package dev.kirby.screenshare;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.kirby.netty.Packet;
import dev.kirby.netty.event.PacketSubscriber;
import dev.kirby.netty.io.Responder;
import dev.kirby.screenshare.commands.ScreenShareCommand;
import dev.kirby.screenshare.listener.PlayerListener;
import dev.kirby.screenshare.netty.ScreenShareServer;
import dev.kirby.screenshare.netty.ServerEvents;
import dev.kirby.screenshare.packet.ConnectPacket;
import dev.kirby.screenshare.packet.PacketSender;
import dev.kirby.screenshare.packet.registry.Registry;
import dev.kirby.screenshare.player.SSManager;
import dev.kirby.screenshare.player.Session;
import dev.kirby.service.ServiceManager;
import lombok.Getter;
import org.slf4j.Logger;

import javax.sql.ConnectionEvent;

@Plugin(
        id = "kirbyvelocity",
        name = "KirbyVelocity",
        version = "1.0"
        , authors = {"SweetyDreams_"}
)
@Getter
public class KirbyVelocity implements ServiceHelper {

    protected static final ServiceManager MANAGER = new ServiceManager();

    @Inject
    private final Logger logger;
    @Inject
    private final ProxyServer server;

    @Inject
    public KirbyVelocity(Logger logger, ProxyServer server) {
        this.logger = logger;
        this.server = server;
        serverSS.getEventRegistry().registerEvents(serverEvents, packetSender);
        serverSS.bind(6990);
        install(SSManager.class, manager);
        install(ProxyServer.class, server);
    }

    private final SSManager manager = new SSManager();
    private final Session.Manager sessionManager = new Session.Manager();

    private final ScreenShareServer serverSS = new ScreenShareServer(Registry.get(), channel -> channel.writeAndFlush(new ConnectPacket()));
    private final ServerEvents serverEvents = new ServerEvents(this, manager, sessionManager);

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {

        //todo packeteventsss
        server.getEventManager().register(this, new PlayerListener(manager, sessionManager));

        CommandManager commandManager = server.getCommandManager();
        commandManager.register(commandManager.metaBuilder("ss").plugin(this).build(), new ScreenShareCommand(sessionManager, manager, server));
    }

    private final Object packetSender = new Object() {
        private Responder responder;
        private final PacketSender packetSender = this::sendPacket;

        @PacketSubscriber
        public void onConnect(final ConnectionEvent packet, final Responder responder) {
            install(Responder.class, this.responder = responder);
            install(PacketSender.class, packetSender);
        }

        public void sendPacket(final Packet packet) {
            if (responder == null) return;
            responder.respond(packet);
        }
    };
}
