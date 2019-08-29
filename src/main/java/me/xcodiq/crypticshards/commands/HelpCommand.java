package me.xcodiq.crypticshards.commands;

import me.xcodiq.crypticshards.Core;
import me.xcodiq.crypticshards.commands.base.CommandBase;
import me.xcodiq.crypticshards.commands.base.CommandHandler;
import me.xcodiq.crypticshards.utilities.ChatUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class HelpCommand extends CommandBase {

    public HelpCommand() {
        super("help", null, "Get a list of all available commands",
                null, 0, 1);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        FileConfiguration config = Core.getInstance().getConfig();
        if (args.length == 0) {
            for (String lines : config.getStringList("messages.shards-help")) {
                sender.sendMessage(ChatUtils.format(lines));
            }
        } else if (args.length == 1) {
            if (sender.hasPermission("crypticshards.admin")) {
                if (args[0].equalsIgnoreCase("admin")) {
                    sender.sendMessage(ChatUtils.format("&8&m-+----------------------------------+-"));
                    sender.sendMessage(ChatUtils.format(" "));
                    sender.sendMessage(ChatUtils.format(" &2&l[!!] &2All commands"));
                    sender.sendMessage(ChatUtils.format(" "));
                    for (CommandBase command : CommandHandler.getCommands()) {
                        sender.sendMessage(ChatUtils.format("   &a&l* &2&l/shards " + command.getName() + " &7- &f" + command.getDescription()));
                    }
                    sender.sendMessage(ChatUtils.format(" "));
                    sender.sendMessage(ChatUtils.format("&8&m-+----------------------------------+-"));
                }
            }
        }
    }
}
