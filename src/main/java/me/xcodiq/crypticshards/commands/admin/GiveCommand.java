package me.xcodiq.crypticshards.commands.admin;

import me.xcodiq.crypticshards.Core;
import me.xcodiq.crypticshards.commands.base.CommandBase;
import me.xcodiq.crypticshards.shard.api.ShardAPI;
import me.xcodiq.crypticshards.utilities.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class GiveCommand extends CommandBase {

    public GiveCommand() {
        super("give", "crypticshards.command.give", "Give item-shards to a player",
                null, 0, 2);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        FileConfiguration config = Core.getInstance().getConfig();
        if (args.length < 2) {
            sender.sendMessage(ChatUtils.format(config.getString("messages.give-wrong-args")));
        } else if (args.length == 2) {
            Player target = Bukkit.getPlayerExact(args[0]);

            if (target == null) {
                sender.sendMessage(ChatUtils.format(config.getString("messages.player-not-online")));
            } else {
                try {
                    int amount = Integer.parseInt(args[1]);
                    if (amount <= 0) {
                        sender.sendMessage(ChatUtils.format(config.getString("messages.no-valid-number")
                                .replace("%number%", Integer.toString(amount))));
                    } else {
                        ShardAPI api = new ShardAPI();
                        api.give(target, amount);
                        sender.sendMessage(ChatUtils.format(config.getString("messages.give-success")
                                .replace("%player%", target.getName())
                                .replace("%shards%", Integer.toString(amount))));
                    }
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatUtils.format(config.getString("messages.no-valid-number")
                            .replace("%number%", args[1])));
                }
            }
        }
    }
}
