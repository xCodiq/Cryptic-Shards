package me.xcodiq.crypticshards.shard.api;

import java.util.Map;

public interface IShard {

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
