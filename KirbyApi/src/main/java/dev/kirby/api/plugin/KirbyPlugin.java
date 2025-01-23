package dev.kirby.api.plugin;

import dev.kirby.KirbyResource;
import dev.kirby.api.util.ApiService;
import dev.kirby.service.ServiceRegistry;
import dev.kirby.utils.Initializer;
import dev.kirby.utils.KirbyLogger;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class KirbyPlugin extends KirbyResource implements Initializer, ApiService {

    protected final KirbyInstance<? extends KirbyPlugin> instance;
    private final KirbyLogger logger;

    public KirbyPlugin(KirbyInstance<? extends KirbyPlugin> kirby) {
        super(kirby.getName(), kirby.getPluginMeta().getVersion());
        this.instance = kirby;
        this.logger = new KirbyLogger(this.name);
    }

    protected void connect() {
        licenseClient.setInfo(this);
        licenseClient.connect();
    }

    public void init() {
    }

    public abstract void enable();

    public abstract void shutdown();

    @Override
    public void destroy() {
        instance.destroy();
    }

    @Override
    public ServiceRegistry manager() {
        return ApiService.super.manager();
    }



}
