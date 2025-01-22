package dev.kirby.screenshare.utils;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.kirby.utils.ColorUtils;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class ServerUtils {

    public RegisteredServer getServer(ProxyServer proxy, String name) {
        return proxy.getServer(name).orElse(null);
    }

    public static @NotNull Component component(Object o) {
        return Component.text(ColorUtils.color(o));
    }


}
