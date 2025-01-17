package dev.kirby.general;

import dev.kirby.netty.Packet;
import dev.kirby.netty.io.Responder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GeneralSender {

    private Responder responder;

    public void sendPacket(final Packet packet) {
        if (responder == null) return;
        responder.respond(packet);
    }

    /*todo remake:
        on connect: send Connect Packet and register packetSender -> connection on class init
        for api: make client.login(infos)




     */
}
