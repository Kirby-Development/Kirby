package dev.kirby.packet;

import dev.kirby.netty.Packet;
import dev.kirby.netty.buffer.PacketBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public enum Status {
    VALID,
    MAX_IP,
    EXPIRED,
    INVALID,
    INVALID_IP,
    INVALID_KEY,
    INVALID_USER,
    INVALID_RESOURCE,
    TOO_MANY_ATTEMPTS;

    public boolean valid() {
        return this == VALID;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class ResponsePacket extends Packet {

        private Status status;

        @Override
        public void read(PacketBuffer buffer) {
            status = buffer.readEnum(Status.class);
        }

        @Override
        public void write(PacketBuffer buffer) {
            buffer.writeEnum(status);
        }
    }
}
