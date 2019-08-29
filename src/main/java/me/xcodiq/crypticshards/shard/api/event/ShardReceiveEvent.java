package me.xcodiq.crypticshards.shard.api.event;

import me.xcodiq.crypticshards.shard.Shard;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This event is called when a player received shards when someone executed /shards give <player> <shards>
 *
 * @see me.xcodiq.crypticshards.shard.api.ShardAPI#give(Player, int)
 */
public class ShardReceiveEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private Shard shard;
    private int amount;
    private boolean cancel;

    public ShardReceiveEvent(Player player, Shard shard, int amount) {
        this.player = player;
        this.shard = shard;
        this.amount = amount;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Get the player who received the shards
     *
     * @return The player who received shards
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Get the shard which a player received
     *
     * @return The received shard
     * @see Shard for all methods such as {@link Shard#getDisplayName()}
     */
    public Shard getShard() {
        return shard;
    }

    /**
     * Get the amount of shards a player received
     *
     * @return The amount of shards
     */
    public int getAmount() {
        return amount;
    }
}