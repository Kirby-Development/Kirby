package dev.kirby;

import dev.kirby.api.plugin.KirbyInstance;
import dev.kirby.api.plugin.KirbyPlugin;
import dev.kirby.config.ConfigManager;
import dev.kirby.config.License;
import lombok.Getter;

@Getter
public class Kirby extends KirbyPlugin {

    private final ConfigManager<License> configManager;

    public Kirby(KirbyInstance<? extends KirbyPlugin> plugin) {
        super(plugin);
        configManager = new ConfigManager<>(plugin.getDataFolder(), new License());
        configManager.load();
        connect();
    }

    @Override
    public void init() {
        getLogger().info("Kirby init");
    }

    @Override
    public void enable() {
        getLogger().info("Kirby enable");
    }

    @Override
    public void shutdown() {
        getLogger().info("Kirby shutdown");
    }


}
