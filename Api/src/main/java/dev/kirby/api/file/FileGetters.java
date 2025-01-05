package dev.kirby.api.file;

import net.kyori.adventure.text.Component;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface FileGetters {
    Component getComponent(String path);

    String getString(@NotNull String path);

    @NotNull List<String> getStringList(@NotNull String path);

    List<ItemStack> getItemStackList(String path);

    <T extends ConfigurationSerializable> List<T> getSerializableList(String path);
}
