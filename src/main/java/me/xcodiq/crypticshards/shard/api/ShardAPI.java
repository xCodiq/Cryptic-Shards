package me.xcodiq.crypticshards.shard.api;

import me.xcodiq.crypticshards.Core;
import me.xcodiq.crypticshards.shard.Shard;
import me.xcodiq.crypticshards.shard.ShardLoader;
import me.xcodiq.crypticshards.shard.api.event.ShardChangeEvent;
import me.xcodiq.crypticshards.shard.api.event.ShardChangeType;
import me.xcodiq.crypticshards.shard.api.event.ShardReceiveEvent;
import me.xcodiq.crypticshards.utilities.database.DatabaseProvider;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import java.util.Map;

public class ShardAPI implements IShard {

    private DatabaseProvider database = Core.getInstance().getShardDatabase();
    private PluginManager pluginManager = Core.getInstance().getServer().getPluginManager();

    /**
     * Check if a player already exists in the database
     *
     * @param uuid The uuid of the player
     * @return true if the player already exists, false otherwise
     */
    @Override
    public boolean existUser(String uuid) {
        return database.existUser(uuid);
    }

    /**
     * Insert a player into the chosen databse, inserting on join {@link me.xcodiq.crypticshards.listener.PlayerJoinListener}
     *
     * @param uuid   The uuid of the player
     * @param shards The shards to insert, most likely 0 at the start
     * @see me.xcodiq.crypticshards.utilities.database.YAML
     * @see me.xcodiq.crypticshards.utilities.database.mysql.MySQL
     */
    @Override
    public void insertUser(String uuid, int shards) {
        database.insertUser(uuid, shards);
    }

    /**
     * Get the amount of shards from a player account
     *
     * @param uuid The uuid of the player
     * @return The shards of the player
     */
    @Override
    public int getShards(String uuid) {
        return database.getShards(uuid);
    }

    /**
     * Reset the shards of a player account back to 0
     *
     * @param uuid The uuid of a player
     */
    @Override
    public void resetShards(String uuid) {
        database.resetShards(uuid);
        pluginManager.callEvent(new ShardChangeEvent(uuid, 0, ShardChangeType.RESET));
    }

    /**
     * Check if a player has the right amount of shards
     *
     * @param uuid   The uuid of the player
     * @param shards The minimum amount of shards they need
     * @return true if they have the shards, false otherwise
     */
    @Override
    public boolean hasShards(String uuid, int shards) {
        return database.hasShards(uuid, shards);
    }

    /**
     * Set the shards of a player account
     *
     * @param uuid   The uuid of the player
     * @param shards The shards to set
     */
    @Override
    public void setShards(String uuid, int shards) {
        database.setShards(uuid, shards);
        pluginManager.callEvent(new ShardChangeEvent(uuid, shards, ShardChangeType.SET));
    }

    /**
     * Add shards to a player account
     *
     * @param uuid   The uuid of the player
     * @param shards The shards to add
     */
    @Override
    public void addShards(String uuid, int shards) {
        database.addShards(uuid, shards);
        pluginManager.callEvent(new ShardChangeEvent(uuid, shards, ShardChangeType.ADD));
    }

    /**
     * Remove shards from a player account
     *
     * @param uuid   The uuid of the player
     * @param shards The shards to remove
     */
    @Override
    public void removeShards(String uuid, int shards) {
        database.removeShards(uuid, shards);
        pluginManager.callEvent(new ShardChangeEvent(uuid, shards, ShardChangeType.REMOVE));
    }

    /**
     * Get the top ten shard accounts
     *
     * @return The top ten shards
     */
    @Override
    public Map<String, Integer> getTopShards() {
        return database.getTopShards();
    }

    /**
     * Give physilly shards to a player in-game
     *
     * @param player The online player
     * @param amount The amount of shards to give
     */
    public void give(Player player, int amount) {
        Shard shards = null;
        for (Shard shard : ShardLoader.getInstance().getShardList()) {
            for (int i = 0; i < amount; i++) {
                shards = shard;
                player.getInventory().addItem(shard.getItemStack());
            }
        }
        pluginManager.callEvent(new ShardReceiveEvent(player, shards, amount));
    }
}
