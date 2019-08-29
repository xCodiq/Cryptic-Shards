package me.xcodiq.crypticshards;

import me.xcodiq.crypticshards.shard.api.ShardAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

class ShardTask {

    private static ShardAPI api = new ShardAPI();

    static void startTask(Core core) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(core, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (api.existUser(player.getUniqueId().toString())) {
                    if (api.getShards(player.getUniqueId().toString()) < 0) {
                        api.setShards(player.getUniqueId().toString(), 0);
                    }
                } else {
                    api.insertUser(player.getUniqueId().toString(), 0);
                }
            }
        }, 0, 600);
    }
}
