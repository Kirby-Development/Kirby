package dev.kirby.api;

import dev.kirby.api.netty.NettyClient;
import dev.kirby.api.service.Registry;
import dev.kirby.api.service.ServiceManager;
import dev.kirby.packet.registry.PacketRegister;
import org.bukkit.plugin.java.JavaPlugin;

public class KirbyApi extends JavaPlugin {

    private static ServiceManager SERVICE_MANAGER = new ServiceManager();

    public static ServiceManager getManager() {
        if (SERVICE_MANAGER == null) SERVICE_MANAGER = new ServiceManager();
        return SERVICE_MANAGER;
    }

    private static Registry REGISTRY = new Registry();
    public static Registry getRegistry() {
        if (REGISTRY == null) REGISTRY = new Registry();
        return REGISTRY;
    }

    private final NettyClient client = new NettyClient(PacketRegister.get(), this::onDisable, "KirbyLicense-Api");

    @Override
    public void onEnable() {
        saveDefaultConfig();
        client.setInfo(new String[]{getName(), getPluginMeta().getVersion()}, getConfig().getString("license"));
        client.connect();
    }

}
