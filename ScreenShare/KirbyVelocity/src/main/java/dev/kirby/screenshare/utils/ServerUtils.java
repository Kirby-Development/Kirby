package dev.kirby.screenshare.utils;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.kirby.Utils;
import dev.kirby.screenshare.KirbyVelocity;
import dev.kirby.screenshare.configuration.Configuration;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class ServerUtils {

    private <T> T get(Class<T> key) {
        return KirbyVelocity.MANAGER.get(key);
    }

    private <T> void install(Class<T> key, T service) {
        KirbyVelocity.MANAGER.put(key, service);
    }

    public RegisteredServer getServer(String name) {
        return get(ProxyServer.class).getServer(get(Configuration.class).getString("servers." + name)).orElse(null);
    }


    public final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");

    public String color(Object o) {
        if (o == null) return "";
        String message = (o instanceof String m) ? m : o.toString();
        if (Utils.maikol(message)) return "";
        return colorize(translateHexColorCodes(message));
    }

    public static @NotNull Component component(Object o) {
        return Component.text(color(o));
    }

    private String colorize(final String message) {
        char[] b = message.toCharArray();
        for (int i = 0; i < b.length - 1; i++) {
            if (b[i] == '&' && "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx".indexOf(b[i + 1]) > -1) {
                b[i] = '\u00A7';
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }
        }
        return new String(b);
    }

    private String translateHexColorCodes(final String message) {
        final char colorChar = '\u00A7';

        final Matcher matcher = HEX_PATTERN.matcher(message);
        final StringBuffer buffer = new StringBuffer(message.length() + 4 * 8);

        while (matcher.find()) {
            final String group = matcher.group(1);

            matcher.appendReplacement(buffer, colorChar + "x"
                    + colorChar + group.charAt(0) + colorChar + group.charAt(1)
                    + colorChar + group.charAt(2) + colorChar + group.charAt(3)
                    + colorChar + group.charAt(4) + colorChar + group.charAt(5));
        }

        return matcher.appendTail(buffer).toString();
    }
}
