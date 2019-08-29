package me.xcodiq.crypticshards.listener;

import me.xcodiq.crypticshards.Core;
import me.xcodiq.crypticshards.shard.Shard;
import me.xcodiq.crypticshards.shard.ShardLoader;
import me.xcodiq.crypticshards.utilities.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class PlayerDropItemListener implements Listener {

    private HashMap<String, Long> dropItem = new HashMap<>();
    private FileConfiguration config = Core.getInstance().getConfig();

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        ItemStack itemStack = event.getItemDrop().getItemStack();
        String displayName = itemStack.getItemMeta().getDisplayName();

        for (Shard shard : ShardLoader.getInstance().getShardList()) {
            if (itemStack.hasItemMeta() && shard.getMaterial().equals(itemStack.getType()) &&
                    displayName.equals(ChatUtils.format(shard.getDisplayName()))) {
                if (!dropItem.containsKey(player.getName())) {

                    player.sendMessage(ChatUtils.format(config.getString("messages.drop-confirm")));
                    dropItem.put(player.getName(), System.currentTimeMillis());
                    Bukkit.getScheduler().runTaskLater(Core.getInstance(), () -> dropItem.remove(player.getName()), 60);
                    event.setCancelled(true);
                    return;
                }
                event.setCancelled(false);
            }
        }
    }
}
