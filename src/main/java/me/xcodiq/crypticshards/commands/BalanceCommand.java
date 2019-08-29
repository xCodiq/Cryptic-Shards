package me.xcodiq.crypticshards.commands;

import me.xcodiq.crypticshards.Core;
import me.xcodiq.crypticshards.commands.base.CommandBase;
import me.xcodiq.crypticshards.shard.api.ShardAPI;
import me.xcodiq.crypticshards.utilities.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class BalanceCommand extends CommandBase {

    public BalanceCommand() {
        super("balance", "crypticshards.command.balance", "Check your shards balance",
                new String[]{"bal", "account"}, 0, 1);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ShardAPI api = new ShardAPI();
        FileConfiguration config = Core.getInstance().getConfig();

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                player.sendMessage(ChatUtils.format(config.getString("messages.balance")
                        .replace("%player%", player.getName())
                        .replace("%shards%", Integer.toString(api.getShards(player.getUniqueId().toString())))));
            } else if (args.length == 1) {
                Player target = Bukkit.getPlayerExact(args[0]);
                if (target == null) {
                    player.sendMessage(ChatUtils.format(config.getString("messages.player-not-online")));
                } else {
                    player.sendMessage(ChatUtils.format(config.getString("messages.balance-other")
                            .replace("%player%", player.getName())
                            .replace("%shards%", Integer.toString(api.getShards(player.getUniqueId().toString())))));
                }
            }
        }
    }
}
