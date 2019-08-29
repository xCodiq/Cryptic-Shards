package me.xcodiq.crypticshards.commands;

import me.xcodiq.crypticshards.Core;
import me.xcodiq.crypticshards.commands.base.CommandBase;
import me.xcodiq.crypticshards.gui.ShardGUI;
import me.xcodiq.crypticshards.utilities.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class OpenGUICommand extends CommandBase {

    public OpenGUICommand() {
        super("opengui", "crypticshards.command.opengui", "Open the main shard gui",
                new String[]{"openmenu"}, 0, 1);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        FileConfiguration config = Core.getInstance().getConfig();

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                ShardGUI gui = new ShardGUI(player);
                gui.openGUI(player);
            } else if (args.length == 1) {
                if (player.hasPermission("crypticshards.command.opengui.other")) {
                    Player target = Bukkit.getPlayerExact(args[0]);
                    if (target == null) {
                        player.sendMessage(ChatUtils.format(config.getString("messages.player-not-online")));
                    } else {
                        ShardGUI gui = new ShardGUI(target);
                        gui.openGUI(target);
                        player.sendMessage(ChatUtils.format(config.getString("messages.let-player-open")
                                .replace("%player%", target.getName())));
                    }
                }
            }
        } else {
            if (args.length < 1) {
                sender.sendMessage(ChatUtils.format("&d&l(!!) &d/shards opengui <player>"));
            } else if (args.length == 1) {
                Player target = Bukkit.getPlayerExact(args[0]);
                if (target == null) {
                    sender.sendMessage(ChatUtils.format(config.getString("messages.player-not-online")));
                } else {
                    ShardGUI gui = new ShardGUI(target);
                    gui.openGUI(target);
                    sender.sendMessage(ChatUtils.format(config.getString("messages.let-player-open")
                            .replace("%player%", target.getName())));
                }
            }
        }
    }
}
