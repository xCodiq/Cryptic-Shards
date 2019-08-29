package me.xcodiq.crypticshards.gui.base.animation;

import me.xcodiq.crypticshards.Core;
import me.xcodiq.crypticshards.gui.base.GUI;
import me.xcodiq.crypticshards.gui.base.GUIComponent;
import me.xcodiq.crypticshards.gui.base.events.GUICloseEvent;
import me.xcodiq.crypticshards.gui.base.events.GUIOpenEvent;
import me.xcodiq.crypticshards.utilities.ChatUtils;
import me.xcodiq.crypticshards.utilities.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class ItemAnimation implements Listener {

    private GUI gui;
    private Integer taskID;
    private Integer ticksBetween;
    private Integer slot;
    private Boolean randomOrder;
    private Boolean reverseOnEnd;
    private Inventory inventory;
    private GUIComponent component;
    private List<ItemStack> itemStack;
    private Supplier<ItemStack> itemStackSupplier;

    public ItemAnimation(List<ItemStack> itemStack, Integer ticksBetween, Boolean reverseOnEnd) {
        this.itemStack = itemStack;
        this.ticksBetween = ticksBetween;
        this.reverseOnEnd = reverseOnEnd;
        this.randomOrder = false;

        register();
    }

    public ItemAnimation(List<ItemStack> itemStack, Integer ticksBetween, Boolean reverseOnEnd, Boolean randomOrder) {
        this.itemStack = itemStack;
        this.ticksBetween = ticksBetween;
        this.reverseOnEnd = reverseOnEnd;
        this.randomOrder = randomOrder;

        register();
    }

    public ItemAnimation(Supplier<ItemStack> itemStackSupplier, Integer ticksBetween) {
        this.itemStackSupplier = itemStackSupplier;
        this.ticksBetween = ticksBetween;
        this.reverseOnEnd = true;
        this.randomOrder = true;

        register();
    }

    public ItemAnimation(Supplier<ItemStack> itemStackSupplier, Integer ticksBetween, Integer slot) {
        this.itemStackSupplier = itemStackSupplier;
        this.ticksBetween = ticksBetween;
        this.reverseOnEnd = true;
        this.randomOrder = true;
        this.slot = slot;

        register();
    }

    private void register() {
        Bukkit.getPluginManager().registerEvents(this, Core.getInstance());
    }

    private void playAnimation() {
        if (itemStack != null) {
            final int[] counter = {0};

            this.taskID = new Random().nextInt(10);
            Bukkit.getScheduler().scheduleAsyncRepeatingTask(Core.getInstance(), () -> {
                if (!randomOrder) {
                    if (!reverseOnEnd) {
                        if (itemStack.size() == counter[0]) {
                            stopAnimation();
                        }
                    } else {
                        if (itemStack.size() == counter[0]) {
                            counter[0] = 0;
                        }
                    }

                    inventory.setItem(component.getSlot(), itemStack.get(counter[0]));
                    counter[0]++;
                } else {
                    counter[0]++;

                    if (!reverseOnEnd) {
                        if (itemStack.size() == counter[0]) {
                            stopAnimation();
                        }
                    }

                    int index = new Random().nextInt(itemStack.size() - 1);
                    inventory.setItem(component.getSlot(), itemStack.get(index));
                }
            }, 0, ticksBetween);
        } else {
            this.taskID = new Random().nextInt(10);
            Bukkit.getScheduler().scheduleAsyncRepeatingTask(Core.getInstance(), () -> {
                ItemStack itemStack = new ItemBuilder(Material.BARRIER).setName(ChatUtils.format("&cERROR!")).toItemStack();
                if (this.itemStackSupplier != null) itemStack = this.itemStackSupplier.get();
                if (itemStack == null) new ItemBuilder(Material.AIR);
                try {
                    inventory.setItem(component.getSlot(), itemStack);
                } catch (NullPointerException ignored) {
                }
            }, 0, ticksBetween);
        }
    }

    private void stopAnimation() {
        if (this.taskID != null) Bukkit.getScheduler().cancelTask(this.taskID);
    }

    @EventHandler
    public void onOpen(GUIOpenEvent event) {
        if (event.getGUI() == gui) {
            inventory = event.getInventory();
            playAnimation();
        }
    }

    @EventHandler
    public void onClose(GUICloseEvent event) {
        inventory = null;
        stopAnimation();
    }

    public Boolean getReverseOnEnd() {
        return reverseOnEnd;
    }

    public void setReverseOnEnd(Boolean reverseOnEnd) {
        this.reverseOnEnd = reverseOnEnd;
    }

    public GUI getGui() {
        return gui;
    }

    public void setGui(GUI gui) {
        this.gui = gui;
    }

    public List<ItemStack> getItemStack() {
        return itemStack;
    }

    public void setItemStack(List<ItemStack> itemStack) {
        this.itemStack = itemStack;
    }

    public Boolean getRandomOrder() {
        return randomOrder;
    }

    public void setRandomOrder(Boolean randomOrder) {
        this.randomOrder = randomOrder;
    }

    public GUIComponent getComponent() {
        return component;
    }

    public void setComponent(GUIComponent component) {
        this.component = component;
    }

    public Integer getTaskID() {
        return taskID;
    }

    public void setTaskID(Integer taskID) {
        this.taskID = taskID;
    }

    public Integer getTicksBetween() {
        return ticksBetween;
    }

    public void setTicksBetween(Integer ticksBetween) {
        this.ticksBetween = ticksBetween;
    }

    public Integer getSlot() {
        return slot;
    }

    public Supplier<ItemStack> getItemStackSupplier() {
        return itemStackSupplier;
    }
}
