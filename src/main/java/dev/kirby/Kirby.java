package dev.kirby;

import dev.kirby.api.IKirby;
import dev.kirby.api.test.Test;
import org.bukkit.plugin.java.JavaPlugin;

public final class Kirby extends JavaPlugin implements IKirby {

    @Override
    public void onEnable() {
        install(Test.class, new Test("prova"));
    }
}
