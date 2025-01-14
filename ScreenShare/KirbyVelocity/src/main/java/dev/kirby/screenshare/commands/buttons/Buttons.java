package dev.kirby.screenshare.commands.buttons;


import dev.kirby.screenshare.configuration.Configuration;
import dev.kirby.screenshare.utils.ServerUtils;
import net.kyori.adventure.text.Component;

public enum Buttons {
    Admission,
    Cheating,
    Refuse,
    Clear;

    public Component getText(Configuration config) {
        return ServerUtils.component(config.getString(path() + ".text"));
    }

    public Component getHover(Configuration config) {
        return ServerUtils.component(config.getString(path() + ".hover"));
    }

    public String getDuration(Configuration config) {
        return config.getString(path() + ".duration");
    }

    public String path() {
        return "buttons." + this.name().toLowerCase();
    }
}
