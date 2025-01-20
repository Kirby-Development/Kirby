package dev.kirby.screenshare.netty;

import dev.kirby.general.GeneralNettyServer;
import dev.kirby.netty.registry.IPacketRegistry;
import dev.kirby.packet.empty.PingPacket;
import dev.kirby.service.ServiceRegistry;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

public class ScreenShareServer extends GeneralNettyServer {

    public ScreenShareServer(IPacketRegistry packetRegistry, Consumer<Channel> connect, ServiceRegistry serviceRegistry) {
        super(packetRegistry, connect, serviceRegistry);
    }

    private final Map<Long, Channel> channels = new ConcurrentHashMap<>();

    public void init(final Channel channel) {
        final long id = ThreadLocalRandom.current().nextLong();
        channels.put(id, channel);
        final PingPacket ping = new PingPacket(id);
        channel.writeAndFlush(ping);
    }


}
