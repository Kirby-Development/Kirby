package dev.kirby.api.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@UtilityClass
public class ApiUtils {

    @UtilityClass
    public class Command {

        public void execute(String command) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }
        public void execute(CommandSender sender, String command) {
            Bukkit.dispatchCommand(sender, command);
        }

    }

    @NotNull
    public List<String> onlineNames() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
    }

    public void safeGive(Player p, ItemStack giveItem) {
        int amount = giveItem.getAmount();
        giveItem.setAmount(1);
        for (int i = 0; i < amount; i++) {
            p.getInventory().addItem(giveItem);
        }
    }

    public int freeSlots(Player p) {
        return (int) Arrays.stream(p.getInventory().getStorageContents()).filter(Objects::isNull).count();
    }

    public void addPermission(UUID userUuid, String permission) {
        //LuckPermsProvider.get().getUserManager().modifyUser(userUuid, user -> user.data().add(Node.builder(permission).build()));
    }
}
