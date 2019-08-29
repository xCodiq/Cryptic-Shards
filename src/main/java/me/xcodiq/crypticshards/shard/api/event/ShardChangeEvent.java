package me.xcodiq.crypticshards.shard.api.event;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/**
 * This event is called when the API is used to modify a shard account of a player.
 *
 * @see me.xcodiq.crypticshards.shard.api.ShardAPI
 */
public class ShardChangeEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private String uuid;
    private int shards;
    private ShardChangeType type;

    public ShardChangeEvent(String uuid, int shards, ShardChangeType type) {
        this.uuid = uuid;
        this.shards = shards;
        this.type = type;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Get the shard change type
     *
     * @return The change type
     * @see ShardChangeType
     */
    public ShardChangeType getType() {
        return type;
    }

    /**
     * Get the uuid of the player
     *
     * @return The uuid of the player
     */
    public String getUniqueId() {
        return uuid;
    }

    /**
     * Get the player of the uuid
     *
     * @return The player of the uuid
     * @see Player
     */
    public Player getPlayer() {
        return Bukkit.getPlayer(UUID.fromString(uuid));
    }

    /**
     * Get the amount of shards changed
     *
     * @return The amount of shards
     */
    public int getShards() {
        return shards;
    }
}