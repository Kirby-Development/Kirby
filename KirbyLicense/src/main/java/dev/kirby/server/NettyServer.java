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


@Getter
public class NettyServer extends GeneralNettyServer {

    private final ThreadManager threadManager;
    public NettyServer(IPacketRegistry packetRegistry, ServiceRegistry serviceRegistry, ThreadManager threadManager) {
        super(packetRegistry, c -> {}, serviceRegistry);
        this.threadManager = threadManager;
    }

    @Override
    protected void initChannel(final Channel channel) {
        super.initChannel(channel);
        init(channel);
    }

    public void init(final Channel channel) {
        final PingPacket ping = new PingPacket();
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