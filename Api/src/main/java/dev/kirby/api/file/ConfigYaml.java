package dev.kirby.api.file;


import dev.kirby.api.plugin.KirbyInstance;
import dev.kirby.api.plugin.KirbyPlugin;

public class ConfigYaml extends KirbyFile {

    public ConfigYaml(KirbyInstance<? extends KirbyPlugin> resource) {
        super(resource, "config");
    }

    public String getLicense() {
        String license = getString("license");
        if (license == null) {
            setLicense("INSERT-LICENSE-HERE");
            throw new RuntimeException("License not found in config.yml");
        }
        return license;
    }

    public void setLicense(String license) {
        getConfig().set("license", license);
    }

    @Override
    public void create() {
        getConfig().set("license", "INSERT-LICENSE-HERE");
    }
}
