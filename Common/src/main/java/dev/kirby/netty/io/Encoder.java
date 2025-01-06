package dev.kirby.netty.io;

import dev.kirby.netty.buffer.PacketBuffer;

public interface Encoder {

    void write(PacketBuffer buffer);

}
