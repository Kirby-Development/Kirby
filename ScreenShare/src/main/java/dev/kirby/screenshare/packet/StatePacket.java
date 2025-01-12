package dev.kirby.screenshare.packet;

import dev.kirby.screenshare.PlayerState;
import dev.kirby.netty.Packet;
import dev.kirby.netty.buffer.PacketBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StatePacket extends Packet {

    UUID player;
    PlayerState state;
    Integer ssId;

    @Override
    public void read(PacketBuffer buffer) {
        player = buffer.readUUID();
        state = buffer.readEnum(PlayerState.class);
        ssId = buffer.readInt();
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeUUID(player);
        buffer.writeEnum(state);
        buffer.writeInt(ssId);
    }
}