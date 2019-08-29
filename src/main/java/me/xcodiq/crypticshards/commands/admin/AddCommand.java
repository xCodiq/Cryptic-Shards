package me.xcodiq.crypticshards.commands.admin;

import me.xcodiq.crypticshards.Core;
import me.xcodiq.crypticshards.commands.base.CommandBase;
import me.xcodiq.crypticshards.shard.api.ShardAPI;
import me.xcodiq.crypticshards.utilities.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class AddCommand extends CommandBase {

    public AddCommand() {
        super("add", "crypticshards.command.add", "Add shards to a player account", new String[]{"addshards"}, 0, 2);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        FileConfiguration config = Core.getInstance().getConfig();
        if (args.length < 2) {
            sender.sendMessage(ChatUtils.format(config.getString("messages.add-wrong-args")));
        } else if (args.length == 2) {
            Player target = Bukkit.getPlayerExact(args[0]);
            ShardAPI api = new ShardAPI();

            if (target == null) {
                sender.sendMessage(ChatUtils.format(config.getString("messages.player-not-online")));
            } else {
                try {
                    int amount = Integer.parseInt(args[1]);
                    if (amount <= 0) {
                        sender.sendMessage(ChatUtils.format(config.getString("messages.no-valid-number")
                                .replace("%number%", Integer.toString(amount))));
                    } else {
                        api.addShards(target.getUniqueId().toString(), amount);
                        sender.sendMessage(ChatUtils.format(config.getString("messages.add-success")
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
