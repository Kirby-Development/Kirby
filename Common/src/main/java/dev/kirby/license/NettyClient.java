package dev.kirby.license;

import dev.kirby.KirbyService;
import dev.kirby.general.GeneralNettyClient;
import dev.kirby.netty.registry.IPacketRegistry;
import dev.kirby.packet.LoginPacket;
import dev.kirby.service.ServiceRegistry;
import dev.kirby.utils.Utils;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
public class NettyClient extends GeneralNettyClient {

    public NettyClient(IPacketRegistry packetRegistry, KirbyService kirby, Runnable shutdownHook, ServiceRegistry serviceRegistry) {
        super(packetRegistry, shutdownHook, "KirbyLicense-" + kirby.getName(), serviceRegistry);
        getEventRegistry().registerEvents(new ClientEvents(this, kirby));
    }

    public void setInfo(KirbyService kirby) {
        setInfo(kirby.data(), kirby.getLicense());
    }

    public void connect() {
        super.connect("127.0.0.1", 9900);
    }

    public void setInfo(String[] data, @Nullable String license) {
        setChannelActiveAction(ctx -> ctx.channel().writeAndFlush(new LoginPacket(Utils.getData(), data, license)));
    }

}