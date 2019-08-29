package me.xcodiq.crypticshards.utilities.database;

import me.xcodiq.crypticshards.Core;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class YAML implements DatabaseProvider {

    private Core core = Core.getInstance();

    @Override
    public void initialize(Core core) {
        core.getLogger().info("Initializing playerCache... [YAML]");
    }

    @Override
    public void deinitialize(Core core) {
        core.getLogger().info("De-initializing playerCache... [YAML]");
    }

    @Override
    public boolean existUser(String uuid) {
        return core.playerCacheConfig.getString(uuid) != null;
    }

    @Override
    public void insertUser(String uuid, int shards) {
        core.playerCacheConfig.set(uuid, shards);
        core.savePlayerCache();
    }

    @Override
    public int getShards(String uuid) {
        return core.playerCacheConfig.getInt(uuid);
    }

    @Override
    public void resetShards(String uuid) {
        core.playerCacheConfig.set(uuid, 0);
        core.savePlayerCache();
    }

    @Override
    public boolean hasShards(String uuid, int shards) {
        return core.playerCacheConfig.getInt(uuid) >= shards;
    }

    @Override
    public void setShards(String uuid, int shards) {
        core.playerCacheConfig.set(uuid, shards);
        core.savePlayerCache();
    }

    @Override
    public void addShards(String uuid, int shards) {
        int amount = this.getShards(uuid) + shards;
        core.playerCacheConfig.set(uuid, amount);
        core.savePlayerCache();
    }

    @Override
    public void removeShards(String uuid, int shards) {
        int amount = this.getShards(uuid) - shards;
        core.playerCacheConfig.set(uuid, amount);
        core.savePlayerCache();
    }

    @Override
    public Map<String, Integer> getTopShards() {
        Map<String, Integer> topTen = new LinkedHashMap<>();

        for (String key : core.playerCacheConfig.getKeys(false)) {
            topTen.put(key, Integer.valueOf(core.playerCacheConfig.getString(key)));
        }

        List<Map.Entry<String, Integer>> list = new LinkedList<>(topTen.entrySet());
        list.sort((o1, o2) -> o2.getValue() - o1.getValue());

        topTen.clear();

        for (Map.Entry<String, Integer> aList : list) {
            topTen.put(aList.getKey(), aList.getValue());
        }

        return topTen;
    }
}
