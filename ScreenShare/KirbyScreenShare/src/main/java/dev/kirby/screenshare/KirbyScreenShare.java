package dev.kirby.screenshare;

import dev.kirby.api.packet.PacketEvent;
import dev.kirby.api.plugin.KirbyPlugin;
import dev.kirby.api.util.ServiceHelper;
import dev.kirby.netty.Packet;
import dev.kirby.netty.event.PacketSubscriber;
import dev.kirby.netty.io.Responder;
import dev.kirby.screenshare.packet.ConnectPacket;
import dev.kirby.screenshare.packet.PacketSender;
import dev.kirby.screenshare.packet.registry.Registry;
import dev.kirby.screenshare.listener.PlayerListener;
import dev.kirby.screenshare.listener.ScreenShareEvents;
import dev.kirby.screenshare.player.ScreenShareManager;
import lombok.Getter;

import javax.sql.ConnectionEvent;

@Getter
public class KirbyScreenShare extends KirbyPlugin implements PacketEvent, ServiceHelper {

    private final ScreenShareManager manager;
    private final ScreenShareClient client = new ScreenShareClient(Registry.get(), this::shutdown, "KirbySS");

    public KirbyScreenShare(Instance instance) {
        super(instance);
        manager = new ScreenShareManager(this.instance);
        install(ScreenShareManager.class, manager);
        client.getEventRegistry().registerEvents(new ScreenShareEvents(this, manager), packetSender);
        client.setChannelActiveAction(ctx -> ctx.writeAndFlush(new ConnectPacket()));
    }

    @Override
    public void enable() {
        new PlayerListener().register();
        client.connect("127.0.0.1", 6990);
    }

    @Override
    public void shutdown() {

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
