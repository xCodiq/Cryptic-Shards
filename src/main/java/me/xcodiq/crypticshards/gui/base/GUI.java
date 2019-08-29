package me.xcodiq.crypticshards.gui.base;

import me.xcodiq.crypticshards.Core;
import me.xcodiq.crypticshards.gui.base.animation.ItemAnimation;
import me.xcodiq.crypticshards.gui.base.events.GUICloseEvent;
import me.xcodiq.crypticshards.gui.base.events.GUIOpenEvent;
import me.xcodiq.crypticshards.utilities.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.function.BiConsumer;

public abstract class GUI implements Listener {

    private final LinkedList<Integer> blacklistedSlots = new LinkedList<>();
    private final LinkedList<GUIComponent> components = new LinkedList<>();
    private final LinkedList<Inventory> INVENTORIES = new LinkedList<>();
    private String title = "";
    private Integer size = 54;
    private InventoryHolder holder = null;
    private ItemStack[] content;

    public GUI(String title, Integer size, InventoryHolder holder) {
        this.title = title;
        this.size = size;
        this.holder = holder;

        register();
    }

    public GUI(String title, Integer size) {
        this.title = title;
        this.size = size;

        register();
    }

    public GUI(String title) {
        this.title = title;

        register();
    }

    public void openGUI(Player player) {
        Inventory inv = Bukkit.createInventory(this.holder, this.size, ChatUtils.format(this.title));
        if (content != null) inv.setContents(content);

        blacklistedSlots.forEach(slot -> inv.setItem(slot, new ItemStack(Material.BARRIER)));
        components.forEach(component -> {
            if (component.getSlot() == -1) {
                int to_add = 0;
                for (int i = 0; i <= inv.getSize(); i++) {
                    if (inv.getItem(i) == null) {
                        to_add = i;
                        i = inv.getSize() + 1;
                    }
                }

                component.setSlot(to_add);
                inv.setItem(to_add, component.getItemStack());
            } else {
                inv.setItem(component.getSlot(), component.getItemStack());
            }
        });

        blacklistedSlots.forEach(slot -> inv.setItem(slot, new ItemStack(Material.AIR)));

        INVENTORIES.add(inv);

        GUIOpenEvent.call(player, inv, this);
        player.openInventory(inv);
    }

    private void register() {
        Bukkit.getPluginManager().registerEvents(this, Core.getInstance());
    }

    public void addItem(int position, ItemStack stack, BiConsumer<Player, InventoryClickEvent> bi) {
        components.add(new GUIComponent(position, stack, bi));
    }

    public void addItem(ItemStack stack) {
        components.add(new GUIComponent(components.size(), stack));
    }

    public void addItem(ItemStack stack, BiConsumer<Player, InventoryClickEvent> bi) {
        components.add(new GUIComponent(-1, stack, bi));
    }

    public void addItem(int position, ItemStack stack) {
        components.add(new GUIComponent(position, stack));

        for (Inventory inventory : INVENTORIES) {
            inventory.setItem(position, stack);
        }

    }

    public void addItem(ItemAnimation stack) {
        GUIComponent component = new GUIComponent(stack.getSlot() == null ? -1 : stack.getSlot(), stack);
        components.add(component);

        stack.setGui(this);
        stack.setComponent(component);
    }

    public void addItem(ItemAnimation stack, BiConsumer<Player, InventoryClickEvent> bi) {
        GUIComponent component = new GUIComponent(stack.getSlot() == null ? -1 : stack.getSlot(), stack, bi);
        components.add(component);

        stack.setGui(this);
        stack.setComponent(component);
    }


    public void setContent(ItemStack[] content) {
        this.content = content;
    }

    public void setBlackListSlots(int[] i) {
        Arrays.stream(i).forEach(value -> {
            if (!blacklistedSlots.contains(value)) {
                blacklistedSlots.add(value);
            }
        });
    }

    public LinkedList<Integer> getBlacklistedSlots() {
        return blacklistedSlots;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public InventoryHolder getHolder() {
        return holder;
    }

    public void setHolder(InventoryHolder holder) {
        this.holder = holder;
    }

    public LinkedList<Inventory> getInventories() {
        return INVENTORIES;
    }

    public LinkedList<GUIComponent> getComponents() {
        return components;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory clicked = event.getClickedInventory();

        if (!INVENTORIES.contains(clicked)) return;

        if (INVENTORIES.contains(clicked)) {
            if (event.getCurrentItem() != null) event.setCancelled(true);
        }

        event.setCancelled(true);
        int slot = event.getSlot();

        if (components.size() == 0) return;

        try {
            components.forEach(components -> {
                if (components.getSlot() == slot) {
                    try {
                        components.getClickEvent().accept((Player) event.getWhoClicked(), event);
                    } catch (Exception ignored) {
                    }
                }
            });
        } catch (ConcurrentModificationException ignored) {
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Inventory closing = event.getInventory();

        if (!INVENTORIES.contains(closing)) {
            return;
        }

        INVENTORIES.remove(closing);
        GUICloseEvent.call((Player) event.getPlayer(), closing, this);
        return;
    }
}
