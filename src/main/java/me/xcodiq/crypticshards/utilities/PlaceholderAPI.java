package me.xcodiq.crypticshards.utilities;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.xcodiq.crypticshards.shard.api.ShardAPI;
import org.bukkit.entity.Player;

public class PlaceholderAPI extends PlaceholderExpansion {

    private final String BALANCE = "balance";
    private final String BALANCE_FORMATTED = "balance_formatted";

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) {
            return "";
        }
        ShardAPI api = new ShardAPI();
        switch (identifier) {
            case BALANCE:
                return String.valueOf(api.getShards(player.getUniqueId().toString()));
            case BALANCE_FORMATTED:
                return ChatUtils.convertInt(api.getShards(player.getUniqueId().toString()));
        }
        return null;
    }

    @Override
    public String getIdentifier() {
        return "crypticshards";
    }

    @Override
    public String getAuthor() {
        return "xCodiq";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }
}
