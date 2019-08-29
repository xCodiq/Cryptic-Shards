package me.xcodiq.crypticshards.utilities.database;

import me.xcodiq.crypticshards.Core;

import java.util.Map;

public interface DatabaseProvider {

    void initialize(Core core);

    void deinitialize(Core core);

    boolean existUser(String uuid);

    void insertUser(String uuid, int shards);

    int getShards(String uuid);

    void resetShards(String uuid);

    boolean hasShards(String uuid, int shards);

    void setShards(String uuid, int shards);

    void addShards(String uuid, int shards);

    void removeShards(String uuid, int shards);

    Map<String, Integer> getTopShards();
}
