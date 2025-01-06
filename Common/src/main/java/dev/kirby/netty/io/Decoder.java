package dev.kirby.netty.io;

import dev.kirby.netty.buffer.PacketBuffer;

public interface Decoder {

    void read(PacketBuffer buffer);

}
