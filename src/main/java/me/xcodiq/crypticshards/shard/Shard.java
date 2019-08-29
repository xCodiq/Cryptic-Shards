package me.xcodiq.crypticshards.shard;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Shard {

    private String name;
    private Material material;
    private ItemStack itemStack;
    private String displayName;
    private List<String> lore;

    public Shard(String name, Material material, ItemStack itemStack, String displayName, List<String> lore) {
        this.name = name;

        this.material = material;
        this.itemStack = itemStack;

        this.displayName = displayName;
        this.lore = lore;
    }

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<String> getLore() {
        return lore;
    }
}
