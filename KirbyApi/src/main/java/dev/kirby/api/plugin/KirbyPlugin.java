package dev.kirby.api.plugin;

import dev.kirby.KirbyService;
import dev.kirby.api.file.ConfigYaml;
import dev.kirby.api.util.ApiService;
import dev.kirby.service.ServiceRegistry;
import dev.kirby.utils.Initializer;
import dev.kirby.utils.KirbyLogger;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class KirbyPlugin extends KirbyService implements Initializer, ApiService {

    protected final KirbyInstance<? extends KirbyPlugin> instance;
    private final KirbyLogger logger;
    private final ConfigYaml config;

    public KirbyPlugin(KirbyInstance<? extends KirbyPlugin> kirby) {
        super(kirby.getName(), kirby.getPluginMeta().getVersion());
        this.instance = kirby;
        this.config = new ConfigYaml(this.instance);
        this.logger = new KirbyLogger(this.name);
        client.setInfo(this);
        client.connect();
    }

    public void init() {
    }

    public abstract void enable();

    public abstract void shutdown();

    @Override
    public void disable() {
        instance.shutdown();
    }

    @Override
    public ServiceRegistry manager() {
        return ApiService.super.manager();
    }

    @Override
    public String getLicense() {
        return getConfig().getLicense();
    }
}
