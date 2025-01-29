package dev.kirby;

import dev.kirby.api.plugin.KirbyInstance;
import dev.kirby.api.plugin.KirbyPlugin;
import dev.kirby.config.ConfigManager;
import dev.kirby.config.License;
import dev.kirby.events.BountyEvents;
import dev.kirby.player.BountyManager;
import lombok.Getter;

@Getter
public class KirbyBounty extends KirbyPlugin {

    private final ConfigManager<License> configManager;

    private final BountyManager bountyManager;

    public KirbyBounty(KirbyInstance<? extends KirbyPlugin> plugin) {
        super(plugin);
        configManager = new ConfigManager<>(plugin.getDataFolder(), new License());
        configManager.load();
        connect();

        bountyManager = new BountyManager(this);
    }

    @Override
    public void init() {
        getLogger().info("Kirby init");
        new BountyEvents(this).register();
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
