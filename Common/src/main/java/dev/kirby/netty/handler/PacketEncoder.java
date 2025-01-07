package dev.kirby.netty.handler;

import dev.kirby.netty.Packet;
import dev.kirby.netty.buffer.PacketBuffer;
import dev.kirby.netty.registry.IPacketRegistry;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.EncoderException;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<Packet> {

    private final IPacketRegistry packetRegistry;

    public PacketEncoder(IPacketRegistry packetRegistry) {
        this.packetRegistry = packetRegistry;
    }

    protected void encode(ChannelHandlerContext channelHandlerContext, Packet packet, ByteBuf byteBuf) throws Exception {
        // Get packet id and write into final packet
        int packetId = packetRegistry.getPacketId(packet.getClass());
        if (packetId < 0) throw new EncoderException("Returned PacketId by registry is < 0");
        byteBuf.writeInt(packetId);
        byteBuf.writeLong(packet.getSessionId());

        // Dump packet data into wrapper packet
        PacketBuffer buffer = new PacketBuffer();
        packet.write(buffer);
        byteBuf.writeBytes(buffer);
    }

}