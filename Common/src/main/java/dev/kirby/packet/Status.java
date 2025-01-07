package dev.kirby.packet;

import dev.kirby.netty.buffer.PacketBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public enum Status {
    VALID,
    INVALID,
    INVALID_IP,
    EXPIRED,
    NOT_FOUND;

    public boolean valid() {
        return this == VALID;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Packet extends dev.kirby.netty.Packet {

        private Status status;

        @Override
        public void read(PacketBuffer buffer) {
            status = Status.valueOf(buffer.readUTF8());
        }

        @Override
        public void write(PacketBuffer buffer) {
            buffer.writeUTF8(status.name());
        }
    }
}
