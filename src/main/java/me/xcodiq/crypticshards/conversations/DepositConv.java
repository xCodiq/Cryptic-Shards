package me.xcodiq.crypticshards.conversations;

import me.xcodiq.crypticshards.Core;
import me.xcodiq.crypticshards.shard.Shard;
import me.xcodiq.crypticshards.shard.ShardLoader;
import me.xcodiq.crypticshards.shard.api.ShardAPI;
import me.xcodiq.crypticshards.utilities.ChatUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DepositConv extends NumericPrompt {

    private ShardAPI api = new ShardAPI();
    private Player player;

    private FileConfiguration config = Core.getInstance().getConfig();

    public DepositConv(Player player) {
        this.player = player;
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext context, Number input) {
        try {
            for (Shard shard : ShardLoader.getInstance().getShardList()) {
                if (input.intValue() > this.getAmount(shard.getItemStack(), shard.getDisplayName()) || input.intValue() < 1) {
                    context.getForWhom().sendRawMessage(ChatUtils.format(config.getString("messages.not-enough-to-deposit")
                            .replace("%shards%", Integer.toString(api.getShards(player.getUniqueId().toString())))));
                } else {
                    for (int i = 0; i < input.intValue(); i++) {
                        player.getInventory().removeItem(shard.getItemStack());
                        player.updateInventory();
                    }
                    api.addShards(player.getUniqueId().toString(), input.intValue());
                    context.getForWhom().sendRawMessage(ChatUtils.format(config.getString("messages.deposit-success")
                            .replace("%shards%", Integer.toString(input.intValue()))));
                }
            }
        } catch (NumberFormatException e) {
            context.getForWhom().sendRawMessage(ChatUtils.format(config.getString("messages.cant-deposit")
                    .replace("%shards%", Integer.toString(input.intValue()))));
        }
        return END_OF_CONVERSATION;
    }

    @Override
    public String getPromptText(ConversationContext context) {
        List<String> depoLore = new ArrayList<>();
        for (String lines : config.getStringList("messages.shards-deposit")) {
            depoLore.add(ChatUtils.format(lines.replace("%shards%", Integer.toString(api.getShards(player.getUniqueId().toString()))
                    .replace("%player%", player.getName()))));
        }
        return String.join("\n", depoLore);
    }

    private int getAmount(ItemStack itemStack, String displayName) {
        if (itemStack == null)
            return 0;
        int amount = 0;
        for (int i = 0; i < 36; i++) {
            ItemStack slot = player.getInventory().getItem(i);
            if (slot == null || !slot.isSimilar(itemStack) || !slot.getItemMeta().getDisplayName().equals(ChatUtils.format(displayName))) {
                continue;
            }
            amount += slot.getAmount();
        }
        return amount;
    }
}
