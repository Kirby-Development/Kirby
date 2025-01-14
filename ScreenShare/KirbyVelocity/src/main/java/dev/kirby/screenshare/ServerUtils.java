package dev.kirby.screenshare;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ServerUtils {

    private <T> T get(Class<T> key) {
        return KirbyVelocity.MANAGER.get(key);
    }

    private <T> void install(Class<T> key, T service) {
        KirbyVelocity.MANAGER.put(key, service);
    }


    public RegisteredServer getServer(String name) {
        return get(ProxyServer.class).getServer(name).orElse(null);
    }

}
