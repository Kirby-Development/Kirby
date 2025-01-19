package dev.kirby.api;

import dev.kirby.api.plugin.KirbyInstance;
import dev.kirby.api.plugin.KirbyPlugin;
import dev.kirby.service.ServiceManager;

public class KirbyApi extends KirbyInstance<KirbyApi.KirbyLicense> {

    private static ServiceManager MANAGER;

    public static ServiceManager getManager() {
        if (MANAGER == null) MANAGER = new ServiceManager();
        return MANAGER;
    }

    private final KirbyLicense kirbyLicense = new KirbyLicense(this);

    public static class KirbyLicense extends KirbyPlugin {

        public KirbyLicense(KirbyInstance<? extends KirbyPlugin> kirby) {
            super(kirby);
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
