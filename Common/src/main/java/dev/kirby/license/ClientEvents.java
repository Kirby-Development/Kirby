package dev.kirby.license;

import dev.kirby.KirbyResource;
import dev.kirby.exception.InvalidException;
import dev.kirby.netty.event.PacketSubscriber;
import dev.kirby.packet.empty.PingPacket;
import dev.kirby.packet.empty.ShutdownPacket;
import dev.kirby.packet.registration.Status;
import dev.kirby.packet.text.BroadCastPacket;
import dev.kirby.packet.text.TextPacket;
import dev.kirby.utils.KirbyLogger;
import io.netty.channel.ChannelHandlerContext;

public record ClientEvents(NettyClient client, KirbyResource kirby) {

    @PacketSubscriber
    public void onTextPacket(final TextPacket packet) {
        logger().info("Received \"" + packet.getText() + "\"");
    }

    @PacketSubscriber
    public void onBroadCast(final BroadCastPacket packet) {
        logger().log(packet.getLevel(), packet.getFormat(), (Object[]) packet.getArgs());
    }

    @PacketSubscriber
    public void onStatusPacket(Status.ResponsePacket packet) {
        Status status = packet.getStatus();
        boolean valid = status.valid();
        if (valid) return;
        client.shutdown();
        throw new InvalidException(InvalidException.Type.SESSION);
    }

    @PacketSubscriber
    public void onPingPacket(final PingPacket packet, final ChannelHandlerContext ctx) {
        client.getChannelActiveAction().accept(ctx);
    }

    @PacketSubscriber
    public void onShutdownPacket(final ShutdownPacket packet) {
        client.shutdown();
        throw new InvalidException(InvalidException.Type.SESSION);
    }

    private KirbyLogger logger() {
        return client.getLogger();
    }
}
