package me.xcodiq.crypticshards.shard.api.event;

public enum ShardChangeType {

    REMOVE("Remove"),
    ADD("Add"),
    SET("Set"),
    RESET("Reset");

    private String string;

    ShardChangeType(String string) {
        this.string = string;
    }

    public String toString() {
        return string;
    }
}
