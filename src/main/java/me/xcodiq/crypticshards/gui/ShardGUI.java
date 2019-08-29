package me.xcodiq.crypticshards.gui;

import me.xcodiq.crypticshards.Core;
import me.xcodiq.crypticshards.conversations.DepositConv;
import me.xcodiq.crypticshards.conversations.WithdrawConv;
import me.xcodiq.crypticshards.conversations.factory.ShardConversation;
import me.xcodiq.crypticshards.gui.base.GUI;
import me.xcodiq.crypticshards.gui.base.animation.ItemAnimation;
import me.xcodiq.crypticshards.shard.api.ShardAPI;
import me.xcodiq.crypticshards.utilities.ChatUtils;
import me.xcodiq.crypticshards.utilities.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class ShardGUI extends GUI {

    public ShardGUI(Player player) {
        super("ShardGUI", 27);
        FileConfiguration config = Core.getInstance().getConfig();
        ShardAPI api = new ShardAPI();
        String uuid = player.getUniqueId().toString();

        this.setTitle(ChatUtils.format(config.getString("shardgui.title")));
        this.setSize(config.getInt("shardgui.size"));

        for (String slots : config.getString("shardgui.glass-item.slots").split(",")) {
            int slot = Integer.parseInt(slots);
            addItem(slot, new ItemBuilder(Material.getMaterial(config.getString("shardgui.glass-item.material")),
                    1, (short) config.getInt("shardgui.glass-item.data"))
                    .setName(ChatUtils.format(config.getString("shardgui.glass-item.item.displayname")))
                    .setLore(ChatUtils.format(config.getStringList("shardgui.glass-item.item.lore"))).toItemStack());
        }

        this.addItem(config.getInt("shardgui.withdraw-item.slot"),
                new ItemBuilder(Material.getMaterial(config.getString("shardgui.withdraw-item.material")),
                        1, (short) config.getInt("shardgui.withdraw-item.data"))
                        .setName(ChatUtils.format(config.getString("shardgui.withdraw-item.item.displayname")))
                        .setLore(ChatUtils.format(config.getStringList("shardgui.withdraw-item.item.lore")))
                        .toItemStack(), (p, event) -> {
                    p.closeInventory();
                    new ShardConversation(player, new WithdrawConv(p), "cancel").start();
                }
        );

        this.addItem(new ItemAnimation(() -> {

                    String itemName = ChatUtils.format(config.getString("shardgui.balance-item.item.displayname")
                            .replace("%shards%", Integer.toString(api.getShards(uuid)))
                            .replace("%player%", player.getName())
                    );

                    List<String> balanceLore = ChatUtils.format(config.getStringList("shardgui.balance-item.item.lore")).stream().map(string ->
                            string.replace("%shards%", Integer.toString(api.getShards(uuid)))
                                    .replace("%player%", player.getName())
                    ).collect(Collectors.toList());

                    return new ItemBuilder(Material.getMaterial(config.getString("shardgui.balance-item.material")))
                            .setName(itemName).setLore(balanceLore).toItemStack();

                }, 20, config.getInt("shardgui.balance-item.slot")),

                (p, event) -> {
                    ShopGUI gui = new ShopGUI();
                    gui.openGUI(p);
                }
        );

        this.addItem(config.getInt("shardgui.deposit-item.slot"),
                new ItemBuilder(Material.getMaterial(config.getString("shardgui.deposit-item.material")),
                        1, (short) config.getInt("shardgui.deposit-item.data"))
                        .setName(ChatUtils.format(config.getString("shardgui.deposit-item.item.displayname")))
                        .setLore(ChatUtils.format(config.getStringList("shardgui.deposit-item.item.lore")))
                        .toItemStack(), (p, event) -> {
                    p.closeInventory();
                    new ShardConversation(player, new DepositConv(p), "cancel").start();
                }
        );
    }
}
