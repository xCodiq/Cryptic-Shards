package me.xcodiq.crypticshards.utilities.command;

import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;

public interface RCommand {

    void registerCommand(String command);

    PluginCommand getCommand(String name);

    CommandMap getCommandMap();
}
