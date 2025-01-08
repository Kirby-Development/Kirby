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
    INVALID_IP, //todo user can register allowed ips
    INVALID_KEY,
    KEY_NOT_FOUND,
    INVALID_SERVICE,
    CLIENT_NOT_FOUND,
    ;

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
            status = Status.valueOf(buffer.readUTF8());
        }

        @Override
        public void write(PacketBuffer buffer) {
            buffer.writeUTF8(status.name());
        }
    }
}
