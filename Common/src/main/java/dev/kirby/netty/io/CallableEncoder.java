package dev.kirby.netty.io;

import dev.kirby.netty.buffer.PacketBuffer;

public interface CallableEncoder<T> {

    void write(T data, PacketBuffer buffer);

}
