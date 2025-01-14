package dev.kirby.api.netty;

import dev.kirby.KirbyLogger;
import dev.kirby.netty.event.PacketSubscriber;
import dev.kirby.packet.PingPacket;
import dev.kirby.packet.Status;
import dev.kirby.packet.TextPacket;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.Level;

public record ClientEvents(NettyClient client) {

    @PacketSubscriber
    public void onPacketReceive(TextPacket packet) {
        logger().info("Received \"" + packet.getText() + "\"");
    }

    @PacketSubscriber
    public void onPacketReceive(Status.ResponsePacket packet) {
        Status status = packet.getStatus();
        boolean valid = status.valid();
        logger().log(valid ? Level.INFO : Level.WARN, "Received " + packet.getPacketName() + ": " + status);
        if (!valid)
            client.shutdown();
    }

    @PacketSubscriber
    public void onPacketReceive(final PingPacket packet, final ChannelHandlerContext ctx) {
        client.getChannelActiveAction().accept(ctx);
    }

    private KirbyLogger logger() {
        return client.getLogger();
    }
}
