package dev.kirby.license;

import dev.kirby.KirbyResource;
import dev.kirby.netty.event.PacketSubscriber;
import dev.kirby.packet.empty.PingPacket;
import dev.kirby.packet.empty.ShutdownPacket;
import dev.kirby.packet.Status;
import dev.kirby.packet.TextPacket;
import dev.kirby.exception.InvalidException;
import dev.kirby.utils.KirbyLogger;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.Level;

public record ClientEvents(NettyClient client, KirbyResource kirby) {

    @PacketSubscriber
    public void onTextPacket(TextPacket packet) {
        logger().info("Received \"" + packet.getText() + "\"");
    }

    @PacketSubscriber
    public void onStatusPacket(Status.ResponsePacket packet) {
        Status status = packet.getStatus();
        boolean valid = status.valid();
        logger().log(valid ? Level.INFO : Level.WARN, "Received " + packet.getPacketName() + ": " + status);
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
