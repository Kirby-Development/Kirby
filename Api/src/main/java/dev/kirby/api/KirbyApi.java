package dev.kirby.api;

import dev.kirby.api.plugin.register.APIRegister;
import dev.kirby.api.service.ServiceManager;
import org.bukkit.plugin.java.JavaPlugin;

public class KirbyApi extends JavaPlugin {

    private static ServiceManager SERVICE_MANAGER = new ServiceManager();
    private static APIRegister REGISTER = new APIRegister();

    public static ServiceManager getManager() {
        if (SERVICE_MANAGER == null) SERVICE_MANAGER = new ServiceManager();
        return SERVICE_MANAGER;
    }

    public static APIRegister getRegister() {
        if (REGISTER == null) REGISTER = new APIRegister();
        return REGISTER;
    }

}
