package dev.kirby.api.plugin;

import dev.kirby.api.KirbyApi;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public abstract class KirbyPlugin extends JavaPlugin implements IKirby {

    private final String name;
    public KirbyPlugin(String name) {
        this.name = name;
        KirbyApi.getRegister().install(this);
    }

}
