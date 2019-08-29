package me.xcodiq.crypticshards.shard;

import me.xcodiq.crypticshards.Core;
import me.xcodiq.crypticshards.utilities.ChatUtils;
import me.xcodiq.crypticshards.utilities.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ShardLoader {

    private static ShardLoader instance = null;
    private List<Shard> shardList = new ArrayList<>();

    /**
     * @return An instance of the ShardLoader class
     */
    public static ShardLoader getInstance() {
        if (instance == null) {
            instance = new ShardLoader();
        }
        return instance;
    }

    public void register(Core core) {
        FileConfiguration config = core.getConfig();
        Shard shard;

        String name = config.getString("shard.name");

        String displayName = ChatUtils.format(config.getString("shard.item.displayname"));
        List<String> lore = ChatUtils.format(config.getStringList("shard.item.lore"));

        Material material = Material.getMaterial(config.getString("shard.material"));
        ItemStack itemStack = new ItemBuilder(material).setName(displayName).setLore(lore).toItemStack();

        shard = new Shard(name, material, itemStack, displayName, lore);
        shardList.add(shard);
    }

    public List<Shard> getShardList() {
        return shardList;
    }
}
