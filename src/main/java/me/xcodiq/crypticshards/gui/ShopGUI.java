package me.xcodiq.crypticshards.gui;

import me.xcodiq.crypticshards.Core;
import me.xcodiq.crypticshards.gui.base.GUI;
import me.xcodiq.crypticshards.shard.api.ShardAPI;
import me.xcodiq.crypticshards.utilities.ChatUtils;
import me.xcodiq.crypticshards.utilities.ItemBuilder;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ShopGUI extends GUI {

    public ShopGUI() {
        super("ShopGUI", 9);
        FileConfiguration config = Core.getInstance().getShopFile();
        ShardAPI api = new ShardAPI();
        Economy eco = Core.getInstance().getEconomy();

        this.setTitle(config.getString("gui.title"));
        this.setSize(config.getInt("gui.size"));

        for (String slots : config.getString("gui.glass-item.slots").split(",")) {
            int slot = Integer.parseInt(slots);
            addItem(slot, new ItemBuilder(Material.getMaterial(config.getString("gui.glass-item.material")),
                    1, (short) config.getInt("gui.glass-item.data"))
                    .setName(ChatUtils.format(config.getString("gui.glass-item.item.displayname")))
                    .setLore(ChatUtils.format(config.getStringList("gui.glass-item.item.lore"))).toItemStack());
        }

        ConfigurationSection cs = config.getConfigurationSection("gui.inventory");

        for (String item : cs.getKeys(false)) {

            int slot = cs.getInt(item + ".slot");
            int amount = cs.getInt(item + ".amount");
            short data = (short) cs.getInt(item + ".data");

            boolean enchanted = cs.getBoolean(item + ".enchanted");

            String displayName = ChatUtils.format(cs.getString(item + ".item.displayname"));
            List<String> lore = ChatUtils.format(cs.getStringList(item + ".item.lore"));

            Material material = Material.getMaterial(cs.getString(item + ".material"));

            ItemStack itemStack = new ItemBuilder(material, amount, data).setName(displayName)
                    .setLore(lore).setEnchanted(enchanted).toItemStack();

            ItemStack denyStack = new ItemBuilder(Material.getMaterial(config.getString("gui.deny-item.material")), 1)
                    .setName(ChatUtils.format(config.getString("gui.deny-item.item.displayname")))
                    .setLore(ChatUtils.format(config.getStringList("gui.deny-item.item.lore"))).toItemStack();


            this.addItem(slot, itemStack,
                    (p, event) -> {
                        if (api.hasShards(p.getUniqueId().toString(), cs.getInt(item + ".requirements.shards"))
                                && eco.has(p, cs.getInt(item + ".requirements.vault-economy"))) {
                            if (cs.getBoolean(item + ".requirements.permission.enabled")) {
                                if (!p.hasPermission(cs.getString(item + ".requirements.permission.permission-needed"))) {
                                    this.addItem(slot, denyStack);
                                    Bukkit.getScheduler().scheduleSyncDelayedTask(Core.getInstance(),
                                            () -> this.addItem(slot, itemStack), 60);
                                    return;
                                }
                                proceedPurchase(p, item, cs, api, eco);
                                return;
                            }
                            proceedPurchase(p, item, cs, api, eco);
                        } else {
                            this.addItem(slot, denyStack);
                            Bukkit.getScheduler().scheduleSyncDelayedTask(Core.getInstance(),
                                    () -> this.addItem(slot, itemStack), 60);
                        }
                    });
        }
    }

    private void proceedPurchase(Player p, String item, ConfigurationSection cs, ShardAPI api, Economy eco) {
        for (String command : cs.getStringList(item + ".commands-on-purchase-console")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command
                    .replace("%player%", p.getName()));
        }
        for (String command : cs.getStringList(item + ".commands-on-purchase-player")) {
            p.performCommand(command.replace("%player%", p.getName()));
        }
        if (!(cs.getInt(item + ".requirements.shards") <= 0)) {
            api.removeShards(p.getUniqueId().toString(), cs.getInt(item + ".requirements.shards"));
        }
        if (!(cs.getInt(item + ".requirements.vault-economy") <= 0)) {
            eco.withdrawPlayer(p, cs.getInt(item + ".requirements.vault-economy"));
        }
        p.closeInventory();
    }
}
