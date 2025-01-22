package dev.kirby.api;

import dev.kirby.api.plugin.KirbyInstance;
import dev.kirby.api.plugin.KirbyPlugin;
import dev.kirby.config.ConfigManager;
import dev.kirby.config.License;
import dev.kirby.service.ServiceManager;
import lombok.Getter;

public class KirbyApi extends KirbyInstance<KirbyApi.KirbyLicense> {

    private static ServiceManager MANAGER;

    public static ServiceManager getManager() {
        if (MANAGER == null) MANAGER = new ServiceManager();
        return MANAGER;
    }

    private final KirbyLicense kirbyLicense = new KirbyLicense(this);

    @Getter
    public static class KirbyLicense extends KirbyPlugin {

        private final ConfigManager<License> configManager;

        public KirbyLicense(KirbyInstance<? extends KirbyPlugin> plugin) {
            super(plugin);
            configManager = new ConfigManager<>(plugin.getDataFolder(), new License());
        }

        @Override
        public void enable() {

        }

        @Override
        public void shutdown() {

        }
    }

    @Override
    protected KirbyLicense load() {
        return kirbyLicense;
    }

}
