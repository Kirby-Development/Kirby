package dev.kirby.server;

import dev.kirby.general.GeneralNettyServer;
import dev.kirby.netty.registry.IPacketRegistry;
import dev.kirby.packet.empty.PingPacket;
import dev.kirby.service.ServiceRegistry;
import dev.kirby.thread.ThreadManager;
import io.netty.channel.Channel;
import lombok.Getter;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;


@Getter
public class NettyServer extends GeneralNettyServer {

    private final ThreadManager threadManager;
    public NettyServer(IPacketRegistry packetRegistry, ServiceRegistry serviceRegistry, ThreadManager threadManager) {
        super(packetRegistry, c -> {}, serviceRegistry);
        this.threadManager = threadManager;
    }

    @Override
    protected void initChannel(Channel channel) {
        super.initChannel(channel);
        init(channel);
    }

    private final Map<Long, Channel> channels = new ConcurrentHashMap<>();

    public void init(final Channel channel) {
        final long id = ThreadLocalRandom.current().nextLong();
        channels.put(id, channel);
        final PingPacket ping = new PingPacket(id);
        threadManager.getAvailableProfileThread().execute(() -> {
            SecureRandom random = new SecureRandom();
            while (true) {
                try {
                    long sleepTime = 5 + random.nextInt(16);
                    Thread.sleep(Duration.ofMinutes(sleepTime));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
                channel.writeAndFlush(ping);
            }
        });
    }
}