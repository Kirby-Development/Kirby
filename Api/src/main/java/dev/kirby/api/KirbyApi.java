package dev.kirby.api;

import dev.kirby.api.netty.NettyClient;
import dev.kirby.api.util.InvalidException;
import dev.kirby.netty.event.PacketSubscriber;
import dev.kirby.packet.ServicePacket;
import dev.kirby.packet.registry.PacketRegister;
import dev.kirby.service.ServiceManager;
import org.bukkit.plugin.java.JavaPlugin;

public class KirbyApi extends JavaPlugin {

    private static ServiceManager MANAGER;

    public KirbyApi() {
        client.getEventRegistry().registerEvents(new Object() {
            @PacketSubscriber
            public void onPacket(ServicePacket packet) {
                if (MANAGER != null) throw new InvalidException(InvalidException.Type.SESSION);
                MANAGER = new ServiceManager();
            }
        });
    }

    public static ServiceManager getManager() {
        if (MANAGER == null) throw new InvalidException(InvalidException.Type.CONNECTION);
        return MANAGER;
    }

    private final NettyClient client = new NettyClient(PacketRegister.get(), this::onDisable, "KirbyLicense-Api");

    @Override
    public void onEnable() {
        saveDefaultConfig();
        client.setInfo(new String[]{getName(), getPluginMeta().getVersion()}, getConfig().getString("license"));
        client.connect();
    }

}
