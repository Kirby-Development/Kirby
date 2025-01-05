package dev.kirby.api.plugin;

import dev.kirby.api.KirbyApi;
import dev.kirby.api.file.ConfigYaml;
import dev.kirby.api.util.KirbyLogger;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class KirbyPlugin {
    protected final KirbyInstance<?> instance;
    private final String name;
    private final KirbyLogger logger;

    private final ConfigYaml config;

    public KirbyPlugin(KirbyInstance<?> kirby) {
        this.instance = kirby;
        this.name = this.instance.getName();
        this.config = new ConfigYaml(this.instance);
        this.logger = new KirbyLogger(this.name);
        KirbyApi.getRegistry().install(this);
    }

    protected void init() {
    }

    protected void enable() {
    }

    protected void shutdown() {
    }
}
