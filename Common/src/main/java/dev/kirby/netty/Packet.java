package dev.kirby.netty;

import dev.kirby.netty.io.Decoder;
import dev.kirby.netty.io.Encoder;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ThreadLocalRandom;

@Setter
@Getter
public abstract class Packet implements Encoder, Decoder {

    /**
     * SessionID is used for identification of the packet for use with {@link dev.kirby.netty.io.Responder}
     */
    private long sessionId = ThreadLocalRandom.current().nextLong();

    public String getPacketName() {
        return this.getClass().getSimpleName();
    }

}