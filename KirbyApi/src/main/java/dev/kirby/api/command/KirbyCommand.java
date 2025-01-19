package dev.kirby.api.command;

import dev.kirby.utils.Utils;
import dev.kirby.api.plugin.KirbyInstance;
import dev.kirby.api.plugin.KirbyPlugin;
import dev.kirby.api.util.ApiUtils;
import dev.kirby.api.util.ColorUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public abstract class KirbyCommand {

    private final String name, permission, description;
    protected final KirbyInstance<? extends KirbyPlugin> plugin;
    private final boolean player;
    @Setter
    private String noPermissionMessage;

    private final List<String> aliases;

    public KirbyCommand(KirbyInstance<? extends KirbyPlugin> plugin) {
        this.plugin = plugin;
        if (!getClass().isAnnotationPresent(Info.class))
            throw new RuntimeException("Info annotation not found on " + this.getClass().getSimpleName());

        Info info = this.getClass().getAnnotation(Info.class);
        this.name = info.name();
        this.description = Utils.fanculoMaikol(info.description(), info.name());
        this.permission = info.permission();
        this.noPermissionMessage = info.noPermissionMessage();
        this.player = info.player();

        aliases = new ArrayList<>(Arrays.stream(info.aliases()).toList());
    }


    public void register() {
        Bukkit.getServer().getCommandMap().register(plugin.getName(), command());
    }

    protected void tab(@NotNull CommandSender sender, List<String> commands, @NotNull String[] args){}

    public void execute(CommandSender sender, String[] args){}

    public void execute(Player player, String[] args){}

    protected @NotNull BukkitCommand command() {
        return new BukkitCommand(getName(), getDescription(), "/" + getName(), getAliases()) {
            @Override
            public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
                List<String> commands = new ArrayList<>();
                tab(sender, commands, args);
                if (commands.isEmpty()) commands.addAll(ApiUtils.onlineNames());
                List<String> list = new ArrayList<>();
                for (String n : commands) {
                    if (!n.toLowerCase().contains(args[args.length - 1].toLowerCase())) continue;
                    list.add(n);
                }
                return list;
            }

            @Override
            public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
                if (permission != null && !sender.hasPermission(permission) && noPermissionMessage != null) {
                    sender.sendMessage(ColorUtils.color(noPermissionMessage));
                    return true;
                }
                if (sender instanceof Player p) {
                    KirbyCommand.this.execute(p, args);
                    return true;
                }

                if (player) sender.sendMessage(ColorUtils.color("&cYou must be a player to execute this command."));
                else KirbyCommand.this.execute(sender, args);
                return true;
            }
        };
    }


    @Retention(RetentionPolicy.RUNTIME)
    public @interface Info {
        String name();

        String permission();

        boolean player() default true;

        String description() default "";

        String noPermissionMessage() default "";

        String[] aliases() default "";
    }
}
