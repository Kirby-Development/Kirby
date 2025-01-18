package dev.kirby.general;

import dev.kirby.netty.Packet;
import dev.kirby.netty.event.PacketSubscriber;
import dev.kirby.netty.io.Responder;
import dev.kirby.packet.ConnectPacket;
import dev.kirby.service.ServiceRegistry;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PacketSender {

    private final ServiceRegistry service;
    private Responder responder;

    public PacketSender(ServiceRegistry service) {
        this.service = service;
    }

    public void sendPacket(final Packet packet) {
        if (responder == null) return;
        responder.respond(packet);
    }

    @PacketSubscriber
    public void onConnect(final ConnectPacket packet, final Responder responder) {
        install(Responder.class, this.responder = responder);
        install(dev.kirby.packet.registry.PacketSender.class, this::sendPacket);
    }

    private <T> void install(Class<T> key, T service) {
        manager().put(key, service);
    }

    public ServiceRegistry manager() {
        return service;
    }
}
