package dev.kirby.api.file;


import dev.kirby.api.plugin.KirbyInstance;
import dev.kirby.api.plugin.KirbyPlugin;
import dev.kirby.utils.InvalidException;

public class ConfigYaml extends KirbyFile {

    public ConfigYaml(KirbyInstance<? extends KirbyPlugin> resource) {
        super(resource, "config");
    }

    public String getLicense() {
        String license = getString("license");
        if (license == null) {
            setLicense("INSERT-LICENSE-HERE");
            throw new InvalidException(InvalidException.Type.LICENSE);
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
