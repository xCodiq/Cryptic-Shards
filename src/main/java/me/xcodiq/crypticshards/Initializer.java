package me.xcodiq.crypticshards;

import me.xcodiq.crypticshards.listener.PlayerDropItemListener;
import me.xcodiq.crypticshards.listener.PlayerJoinListener;
import org.bukkit.Bukkit;

import java.util.stream.Stream;

class Initializer {

    Initializer(Core core) {
        Stream.of(
                new PlayerJoinListener(), new PlayerDropItemListener()
        ).forEach(listener -> Bukkit.getServer().getPluginManager().registerEvents(listener, core));
    }
}
