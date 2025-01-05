package dev.kirby.api.plugin.register;

import dev.kirby.api.plugin.IKirby;

import java.util.HashMap;
import java.util.Map;

public class APIRegister {

    private final Map<String, IKirby> PLUGINS = new HashMap<>();

    public void install(IKirby kirbyPlugin) {
        PLUGINS.put(kirbyPlugin.getName(), kirbyPlugin);
    }

    public IKirby get(String name) {
        return PLUGINS.get(name);
    }

}