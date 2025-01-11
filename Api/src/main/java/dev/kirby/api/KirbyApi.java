package dev.kirby.api;

import dev.kirby.api.netty.NettyClient;
import dev.kirby.service.ServiceManager;
import dev.kirby.packet.registry.PacketRegister;
import org.bukkit.plugin.java.JavaPlugin;

public class KirbyApi extends JavaPlugin {

    private static ServiceManager MANAGER = new ServiceManager();

    public static ServiceManager getManager() {
        if (MANAGER == null) MANAGER = new ServiceManager();
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
