package dev.kirby.api.file;


import dev.kirby.api.plugin.KirbyInstance;

public class ConfigYaml extends KirbyFile {

    public ConfigYaml(KirbyInstance<?> resource) {
        super(resource, "config");
    }

}
