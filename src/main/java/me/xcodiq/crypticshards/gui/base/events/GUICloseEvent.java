package me.xcodiq.crypticshards.gui.base.events;

import me.xcodiq.crypticshards.gui.base.GUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;

public class GUICloseEvent extends Event implements Listener {

    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private Player player;
    private Inventory inventory;
    private GUI gui;

    public GUICloseEvent(Player player, Inventory inventory, GUI gui) {
        this.player = player;
        this.inventory = inventory;
        this.gui = gui;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    public static void call(Player player, Inventory inventory, GUI gui) {
        Bukkit.getPluginManager().callEvent(new GUICloseEvent(player, inventory, gui));
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public Player getPlayer() {
        return player;
    }

    public GUI getGUI() {
        return gui;
    }

    public Inventory getInventory() {
        return inventory;
    }
}
