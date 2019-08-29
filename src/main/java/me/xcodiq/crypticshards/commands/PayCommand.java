package me.xcodiq.crypticshards.commands;

import me.xcodiq.crypticshards.Core;
import me.xcodiq.crypticshards.commands.base.CommandBase;
import me.xcodiq.crypticshards.shard.api.ShardAPI;
import me.xcodiq.crypticshards.utilities.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class PayCommand extends CommandBase {

    public PayCommand() {
        super("pay", "crypticshards.command.pay", "Give shards to a player",
                null, 0, 2);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        FileConfiguration config = Core.getInstance().getConfig();

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length < 2) {
                player.sendMessage(ChatUtils.format("&d&l(!!) &d/shards pay <player> <shards>"));
            } else if (args.length == 2) {
                Player target = Bukkit.getPlayerExact(args[0]);
                ShardAPI api = new ShardAPI();

                if (target == null) {
                    player.sendMessage(ChatUtils.format(config.getString("messages.player-not-online")));
                } else if (target == player) {
                    player.sendMessage(ChatUtils.format(config.getString("messages.cant-pay-yourself")));
                } else {
                    try {
                        int amount = Integer.parseInt(args[1]);
                        if (amount <= 0) {
                            player.sendMessage(ChatUtils.format(config.getString("messages.no-valid-number")
                                    .replace("%number%", Integer.toString(amount))));
                        } else {
                            if (!api.hasShards(player.getUniqueId().toString(), amount)) {
                                player.sendMessage(ChatUtils.format(config.getString("messages.not-enough-shards")
                                        .replace("%shards%", Integer.toString(api.getShards(player.getUniqueId()
                                                .toString())))));
                            } else {
                                player.sendMessage(ChatUtils.format(config.getString("messages.given-shards")
                                        .replace("%shards%", Integer.toString(amount))
                                        .replace("%player%", target.getName())));
                                target.sendMessage(ChatUtils.format(config.getString("messages.received-shards")
                                        .replace("%shards%", Integer.toString(amount))
                                        .replace("%player%", player.getName())));
                                api.removeShards(player.getUniqueId().toString(), amount);
                                api.addShards(target.getUniqueId().toString(), amount);
                            }
                        }
                    } catch (NumberFormatException e) {
                        player.sendMessage(ChatUtils.format(config.getString("messages.no-valid-number")
                                .replace("%number%", args[1])));
                    }
                }
            }
        }
    }
}
