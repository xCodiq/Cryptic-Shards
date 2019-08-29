package me.xcodiq.crypticshards.commands.base;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class CommandBase {

    private String name;
    private String permission;
    private String description;

    private List<String> aliases;

    private int minimumArguments;
    private int maximumArguments;


    public CommandBase(String name, String permission, String description, String[] aliases, int minimumArguments, int maximumArguments) {
        this.name = name;
        this.permission = permission;
        this.description = description;

        this.aliases = aliases == null ? new ArrayList<>() : Arrays.asList(aliases);

        this.minimumArguments = minimumArguments;
        this.maximumArguments = maximumArguments;
    }

    public abstract void execute(CommandSender sender, String[] args);

    public String getName() {
        return name;
    }

    public String getPermission() {
        return permission;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public int getMinimumArguments() {
        return minimumArguments;
    }

    public int getMaximumArguments() {
        return maximumArguments;
    }
}
