package dev.kirby.keeby;

import dev.kirby.api.IKirby;
import dev.kirby.api.test.Test;
import org.bukkit.plugin.java.JavaPlugin;

public final class Keeby extends JavaPlugin implements IKirby {

    @Override
    public void onEnable() {
        System.out.println(get(Test.class).text());
    }
}
