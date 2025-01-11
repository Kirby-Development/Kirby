package dev.kirby.api.packet.listener;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import dev.kirby.api.player.processor.Processor;
import org.jetbrains.annotations.NotNull;

import static dev.kirby.api.packet.listener.Side.Receive;
import static dev.kirby.api.packet.listener.Side.Send;

public record Packet(PacketReceiveEvent receive, PacketSendEvent send, Side side) {

    public Packet(PacketReceiveEvent event) {
        this(event, null, Receive);
    }

    public Packet(PacketSendEvent event) {
        this(null, event, Send);
    }

    public PacketTypeCommon type() {
        return switch (side) {
            case Receive -> receive().getPacketType();
            case Send -> send().getPacketType();
        };
    }

    public boolean client() {
        return side == Receive;
    }

    public boolean server() {
        return side == Send;
    }


    public boolean is(PacketTypeCommon type) {
        return type().equals(type);
    }

    private @NotNull IllegalStateException exception() {
        return new IllegalStateException("Packet is " + type().getSide() + " side");
    }

    public void handle(Processor<?> processor) {
        processor.handle(this);
    }

}
