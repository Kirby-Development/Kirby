package dev.kirby.netty.io;

import dev.kirby.netty.buffer.PacketBuffer;

public interface CallableDecoder<T> {

    T read(PacketBuffer buffer);

}
