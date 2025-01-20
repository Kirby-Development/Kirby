package dev.kirby.license;

import dev.kirby.KirbyResource;
import dev.kirby.general.GeneralNettyClient;
import dev.kirby.netty.registry.IPacketRegistry;
import dev.kirby.packet.registration.LoginPacket;
import dev.kirby.packet.registration.LogoutPacket;
import dev.kirby.service.ServiceRegistry;
import dev.kirby.utils.Utils;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
public class NettyClient extends GeneralNettyClient {

    public NettyClient(IPacketRegistry packetRegistry, KirbyResource kirby, Runnable shutdownHook, ServiceRegistry serviceRegistry) {
        super(packetRegistry, shutdownHook, "KirbyLicense-" + kirby.getName(), serviceRegistry);
        getEventRegistry().registerEvents(new ClientEvents(this, kirby));
    }

    public void setInfo(KirbyResource kirby) {
        setInfo(kirby.data(), kirby.getLicense());
    }

    public void connect() {
        super.connect("127.0.0.1", 9900);
    }

    private String[] data;
    private String license;
    public void setInfo(String[] data, @Nullable String license) {
        this.data = data;
        this.license = license;
        setChannelActiveAction(ctx -> ctx.channel().writeAndFlush(new LoginPacket(Utils.getData(), data, license)));
    }

    @Override
    public void shutdown() {
        getPacketSender().sendPacket(new LogoutPacket(Utils.getData(), data, license));
        super.shutdown();
    }
}