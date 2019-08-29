package me.xcodiq.crypticshards.gui.base;

import me.xcodiq.crypticshards.gui.base.animation.ItemAnimation;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;

public class GUIComponent {

    private int slot;
    private ItemStack itemStack;
    private ItemAnimation itemAnimation;
    private BiConsumer<Player, InventoryClickEvent> clickEvent;

    public GUIComponent(int slot, ItemStack itemStack, BiConsumer<Player, InventoryClickEvent> clickEvent) {
        this.slot = slot;
        this.itemStack = itemStack;
        this.clickEvent = clickEvent;
    }

    public GUIComponent(int slot, ItemStack itemStack) {
        this.slot = slot;
        this.itemStack = itemStack;
    }

    public GUIComponent(int slot, ItemAnimation itemAnimation, BiConsumer<Player, InventoryClickEvent> clickEvent) {
        this.slot = slot;
        this.itemStack = itemAnimation.getItemStackSupplier() != null ? itemAnimation.getItemStackSupplier().get() : new ItemStack(Material.BARRIER);
        this.itemAnimation = itemAnimation;
        this.clickEvent = clickEvent;
    }

    public GUIComponent(int slot, ItemAnimation itemAnimation) {
        this.slot = slot;
        this.itemAnimation = itemAnimation;
        this.itemStack = itemAnimation.getItemStackSupplier() != null ? itemAnimation.getItemStackSupplier().get() : new ItemStack(Material.BARRIER);
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public BiConsumer<Player, InventoryClickEvent> getClickEvent() {
        return clickEvent;
    }

    public void setClickEvent(BiConsumer<Player, InventoryClickEvent> clickEvent) {
        this.clickEvent = clickEvent;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemAnimation getItemAnimation() {
        return itemAnimation;
    }

    public void setItemAnimation(ItemAnimation itemAnimation) {
        this.itemAnimation = itemAnimation;
    }
}
