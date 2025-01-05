package dev.kirby.api;

import dev.kirby.api.service.Registry;
import dev.kirby.api.service.ServiceManager;
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



}
