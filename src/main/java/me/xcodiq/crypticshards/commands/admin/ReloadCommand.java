package me.xcodiq.crypticshards.commands.admin;

import me.xcodiq.crypticshards.Core;
import me.xcodiq.crypticshards.commands.base.CommandBase;
import me.xcodiq.crypticshards.utilities.ChatUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class ReloadCommand extends CommandBase {

    public ReloadCommand() {
        super("reload", "crypticshards.command.reload", "Reload all configuration files",
                null, 0, 0);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            Core core = Core.getInstance();
            FileConfiguration config = core.getConfig();
            long start = System.currentTimeMillis();

            core.reloadConfig();
            core.savePlayerCache();
            core.saveConfig();

            sender.sendMessage(ChatUtils.format(config.getString("messages.config-reloaded")
                    .replace("%ping%", Long.toString((System.currentTimeMillis() - start)))));
        }
    }
}
