package dev.kirby;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import org.slf4j.Logger;

@Plugin(
    id = "kirbyvelocity",
    name = "KirbyVelocity",
    version = "1.0"
    ,authors = {"SweetyDreams_"}
)
public class KirbyVelocity {

    @Inject private Logger logger;

    public KirbyVelocity(Logger logger) {
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
    }
}
