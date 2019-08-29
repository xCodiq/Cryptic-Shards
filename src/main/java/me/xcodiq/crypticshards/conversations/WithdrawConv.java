package me.xcodiq.crypticshards.conversations;

import me.xcodiq.crypticshards.Core;
import me.xcodiq.crypticshards.shard.api.ShardAPI;
import me.xcodiq.crypticshards.utilities.ChatUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WithdrawConv extends NumericPrompt {

    private ShardAPI api = new ShardAPI();
    private Player player;

    private FileConfiguration config = Core.getInstance().getConfig();

    public WithdrawConv(Player player) {
        this.player = player;
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext context, Number input) {
        if (input.intValue() > api.getShards(player.getUniqueId().toString()) || input.intValue() < 1) {
            context.getForWhom().sendRawMessage(ChatUtils.format(config.getString("messages.not-enough-to-withdraw")
                    .replace("%shards%", Integer.toString(api.getShards(player.getUniqueId().toString())))));
        } else {
            if (player.getInventory().firstEmpty() == -1) {
                context.getForWhom().sendRawMessage(ChatUtils.format(config.getString("messages.not-enough-inventory-space-to-withdraw")));
            } else {
                api.give(player, input.intValue());
                api.removeShards(player.getUniqueId().toString(), input.intValue());
                context.getForWhom().sendRawMessage(ChatUtils.format(config.getString("messages.withdraw-success")
                        .replace("%shards%", Integer.toString(input.intValue()))));
            }
        }
        return END_OF_CONVERSATION;
    }

    @Override
    public String getPromptText(ConversationContext context) {
        List<String> withdrawLore = new ArrayList<>();
        for (String lines : config.getStringList("messages.shards-withdraw")) {
            withdrawLore.add(ChatUtils.format(lines.replace("%shards%", Integer.toString(api.getShards(player.getUniqueId().toString()))
                    .replace("%player%", player.getName()))));
        }
        return String.join("\n", withdrawLore);
    }

}
