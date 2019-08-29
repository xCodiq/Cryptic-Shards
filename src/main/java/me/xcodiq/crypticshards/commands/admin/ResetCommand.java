package me.xcodiq.crypticshards.commands.admin;

import me.xcodiq.crypticshards.Core;
import me.xcodiq.crypticshards.commands.base.CommandBase;
import me.xcodiq.crypticshards.shard.api.ShardAPI;
import me.xcodiq.crypticshards.utilities.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ResetCommand extends CommandBase {

    public ResetCommand() {
        super("reset", "crypticshards.command.reset", "Reset shards of a player account",
                new String[]{"resetshards"}, 0, 1);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        FileConfiguration config = Core.getInstance().getConfig();
        if (args.length < 1) {
            sender.sendMessage(ChatUtils.format(config.getString("messages.reset-wrong-args")));
        } else if (args.length == 1) {
            Player target = Bukkit.getPlayerExact(args[0]);
            ShardAPI api = new ShardAPI();

            if (target == null) {
                sender.sendMessage(ChatUtils.format(config.getString("messages.player-not-online")));
            } else {
                api.resetShards(target.getUniqueId().toString());
                sender.sendMessage(ChatUtils.format(config.getString("messages.reset-success")
                        .replace("%player%", target.getName())));
            }
        }
    }
}
