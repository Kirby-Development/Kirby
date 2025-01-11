package dev.kirby.api.netty;

import dev.kirby.Utils;
import dev.kirby.api.plugin.KirbyPlugin;
import dev.kirby.general.GeneralNettyClient;
import dev.kirby.netty.registry.IPacketRegistry;
import dev.kirby.packet.LoginPacket;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
public class NettyClient extends GeneralNettyClient {


    public NettyClient(IPacketRegistry packetRegistry, KirbyPlugin kirby, Runnable shutdownHook) {
        this(packetRegistry, shutdownHook, "KirbyLicense-" + kirby.getName());
        setChannelActiveAction(ctx -> ctx.channel().writeAndFlush(new LoginPacket(Utils.getData(), kirby.data(), kirby.getConfig().getLicense())));
    }

    public NettyClient(IPacketRegistry packetRegistry, Runnable shutdownHook, String loggerName) {
        super(packetRegistry, shutdownHook, loggerName);
        getEventRegistry().registerEvents(new ClientEvents(this));
    }


    public void connect() {
        super.connect("127.0.0.1", 9900);
    }

    public void setInfo(String[] data, @Nullable String license) {
        setChannelActiveAction(ctx -> ctx.channel().writeAndFlush(new LoginPacket(Utils.getData(), data, license)));
    }

}
