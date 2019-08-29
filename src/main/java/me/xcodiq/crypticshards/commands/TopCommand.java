package me.xcodiq.crypticshards.commands;

import me.xcodiq.crypticshards.Core;
import me.xcodiq.crypticshards.commands.base.CommandBase;
import me.xcodiq.crypticshards.utilities.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TopCommand extends CommandBase {

    public TopCommand() {
        super("top", "crypticshards.command.top", "Get the leaderboard message",
                null, 0, 0);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            FileConfiguration config = Core.getInstance().getConfig();

            List<String> header = config.getStringList("leaderboard.text-top.header");
            header.forEach((str) -> player.sendMessage(ChatUtils.format(str)));

            Map<String, Integer> map = Core.getInstance().getShardDatabase().getTopShards();
            int size = Math.min(map.size(), 10);

            for (int i = 0; i < size; i++) {
                UUID uuid = UUID.fromString(map.keySet().toArray()[i].toString());
                String name = Bukkit.getServer().getOfflinePlayer(uuid).getName();
                String coins = map.values().toArray()[i].toString();
                String place = String.valueOf(i + 1);
                player.sendMessage(ChatUtils.format(config.getString("leaderboard.text-top.content")
                        .replace("%player%", name)
                        .replace("%place%", place)
                        .replace("%shards_formatted%", ChatUtils.convertInt(Integer.parseInt(coins)))
                        .replace("%shards%", coins)));
            }

            List<String> footer = config.getStringList("leaderboard.text-top.footer");
            footer.forEach((str) -> player.sendMessage(ChatUtils.format(str)));
        }
    }
}