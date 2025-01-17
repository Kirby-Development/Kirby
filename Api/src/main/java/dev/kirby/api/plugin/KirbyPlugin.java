package dev.kirby.api.plugin;

import dev.kirby.Initializer;
import dev.kirby.KirbyLogger;
import dev.kirby.api.file.ConfigYaml;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class KirbyPlugin implements Initializer {

    protected final KirbyInstance<? extends KirbyPlugin> instance;
    private final String name;
    private final String version;
    private final KirbyLogger logger;
    private final ConfigYaml config;

    public KirbyPlugin(KirbyInstance<? extends KirbyPlugin> kirby) {
        this.instance = kirby;
        this.name = this.instance.getName();
        this.version = this.instance.getPluginMeta().getVersion();
        this.config = new ConfigYaml(this.instance);
        this.logger = new KirbyLogger(this.name);
    }

    public void init() {}
    public abstract void enable();
    public abstract void shutdown();

    public String[] data() {
        return new String[]{
                name,
                version
        };
    }
}
