package me.xcodiq.crypticshards.commands;

import me.xcodiq.crypticshards.Core;
import me.xcodiq.crypticshards.commands.base.CommandBase;
import me.xcodiq.crypticshards.utilities.ChatUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;

public class AboutCommand extends CommandBase {

    public AboutCommand() {
        super("about", null, "Get a list of all available commands",
                null, 0, 1);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        PluginDescriptionFile desc = Core.getInstance().getDescription();

        sender.sendMessage(ChatUtils.format("&8&m-+----------------------------------+-"));
        sender.sendMessage(ChatUtils.format(" &2&l[!!] &2Plugin Information"));
        sender.sendMessage(ChatUtils.format(" "));
        sender.sendMessage(ChatUtils.format("   &a&l* &2&lName &7" + desc.getName()));
        sender.sendMessage(ChatUtils.format("   &a&l* &2&lVersion &7" + desc.getVersion()));
        sender.sendMessage(ChatUtils.format("   &a&l* &2&lAuthors &7" + String.join("&7, &7", desc.getAuthors())));
        sender.sendMessage(ChatUtils.format("   &a&l* &2&lDeveloper &axCodiq"));
        sender.sendMessage(ChatUtils.format("   &a&l* &2&lWebsite &7" + desc.getWebsite()));
        sender.sendMessage(ChatUtils.format(" "));
        sender.sendMessage(ChatUtils.format(" &7(( &f" + desc.getDescription() + " &7))"));
        sender.sendMessage(ChatUtils.format("&8&m-+----------------------------------+-"));

    }
}
