package me.xcodiq.crypticshards.listener;

import me.xcodiq.crypticshards.Core;
import me.xcodiq.crypticshards.utilities.database.DatabaseProvider;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        DatabaseProvider database = Core.getInstance().getShardDatabase();
        if (!database.existUser(player.getUniqueId().toString())) {
            database.insertUser(player.getUniqueId().toString(), 0);
        }
    }
}
