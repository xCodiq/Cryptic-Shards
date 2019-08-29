package me.xcodiq.crypticshards.commands.base;

import me.xcodiq.crypticshards.Core;
import me.xcodiq.crypticshards.commands.*;
import me.xcodiq.crypticshards.commands.admin.*;
import me.xcodiq.crypticshards.gui.ShardGUI;
import me.xcodiq.crypticshards.utilities.ChatUtils;
import me.xcodiq.crypticshards.utilities.IHandler;
import me.xcodiq.crypticshards.utilities.command.RCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandHandler implements CommandExecutor, TabCompleter, IHandler, RCommand {

    private static List<CommandBase> commands;
    private String yourCommand, permission, permissionMessage, description;
    private List<String> aliases;

    public CommandHandler(String yourCommand, String permission, String permissionMessage, String description, String[] aliases) {
        this.yourCommand = yourCommand;

        this.permission = permission;
        this.permissionMessage = permissionMessage;
        this.description = description;

        this.aliases = aliases == null ? new ArrayList<>() : Arrays.asList(aliases);
    }

    public static List<CommandBase> getCommands() {
        return commands;
    }

    @Override
    public void enable(Core core) {
        commands = new ArrayList<>();

        this.registerCommand(yourCommand);
        core.getCommand(yourCommand).setExecutor(this);

        Stream.of(
                new HelpCommand(), new GiveCommand(), new BalanceCommand(), new TopCommand(),
                new ShopCommand(), new OpenGUICommand(), new ReloadCommand(), new PayCommand(),
                new AddCommand(), new ResetCommand(), new RemoveCommand(), new SetCommand(),
                new WithdrawCommand(), new DepositCommand(), new AboutCommand()
        ).forEach(this::register);
    }

    @Override
    public void disable(Core core) {
        commands.clear();
        commands = null;
    }

    private void register(CommandBase commandBase) {
        commands.add(commandBase);
    }

    /**
     * Executes the command, returning its success
     *
     * @param sender Source object which is executing this command
     * @param cmd    The alias of the command used
     * @param args   All arguments passed to the command, split via ' '
     * @return true if the command was successful, otherwise false
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        FileConfiguration config = Core.getInstance().getConfig();
        PluginDescriptionFile desc = Core.getInstance().getDescription();

        if (!cmd.getName().equalsIgnoreCase(yourCommand)) {
            return true;
        }
        if (args.length == 0 || args[0].isEmpty()) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (config.getBoolean("shardgui.open-on-command")) {
                    ShardGUI gui = new ShardGUI(player);
                    gui.openGUI(player);
                    return true;
                }
                player.sendMessage(ChatUtils.format("&2&l[!!] &a" + desc.getFullName() + " &2by &axCodiq"));
                player.sendMessage(ChatUtils.format(" &8➥ &7Type &n/" + label + " help&7 for all commands!"));
            } else {
                sender.sendMessage(ChatUtils.format("&2&l[!!] &2Type /" + label + " help for all commands"));
            }
        }
        for (CommandBase command : commands) {
            if (args.length != 0) {
                if (!command.getName().equalsIgnoreCase(args[0]) &&
                        !command.getAliases().contains(args[0].toLowerCase())) {
                    continue;
                }
            } else {
                continue;
            }
            if (command.getPermission() != null) {
                if (!sender.hasPermission(command.getPermission())
                        && (!sender.hasPermission("crypticshards.admin")
                        || !sender.hasPermission("crypticshards.command.*"))) {
                    sender.sendMessage(ChatUtils.format(config.getString("messages.no-permission")
                            .replace("%command%", "/shards " + command.getName())));
                    return true;
                }
            }

            args = Arrays.copyOfRange(args, 1, args.length);

            if ((command.getMinimumArguments() != -1 && command.getMinimumArguments() > args.length)
                    || (command.getMaximumArguments() != -1 && command.getMaximumArguments() < args.length)) {
                sender.sendMessage(ChatUtils.format(config.getString("messages.wrong-arguments")
                        .replace("%command%", label)));
            }
            command.execute(sender, args);
            return true;
        }
        return true;
    }

    /**
     * Executed on tab completion for this command, returning a list of
     * options the player can tab through.
     *
     * @param sender Source object which is executing this command
     * @param alias  the alias being used
     * @param cmd    the command being executed
     * @param args   All arguments passed to the command, split via ' '
     * @return a list of tab-completions for the specified arguments. This
     * will never be null. List may be immutable.
     * @throws IllegalArgumentException if sender, alias, or args is null
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (cmd.getName().equalsIgnoreCase(yourCommand)) {
            if (args.length == 1) {
                List<String> commandNames = new ArrayList<>();
                if (!args[0].equals("")) {
                    for (String commandName : commands.stream().map(CommandBase::getName).collect(Collectors.toList())) {
                        if (!commandName.startsWith(args[0].toLowerCase())) {
                            continue;
                        }
                        commandNames.add(commandName);
                    }
                } else {
                    commandNames = commands.stream().map(CommandBase::getName).collect(Collectors.toList());
                }
                Collections.sort(commandNames);
                return commandNames;
            }
        }
        return null;
    }

    @Override
    public void registerCommand(String command) {
        PluginCommand cmd = this.getCommand(command);

        cmd.setName(yourCommand);
        cmd.setPermission(permission);
        cmd.setPermissionMessage(permissionMessage);
        cmd.setDescription(description);
        cmd.setAliases(aliases);

        this.getCommandMap().register(Core.getInstance().getDescription().getName(), cmd);
    }

    @Override
    public PluginCommand getCommand(String name) {
        PluginCommand command = null;

        try {
            Constructor<PluginCommand> c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            c.setAccessible(true);

            command = c.newInstance(name, Core.getInstance());
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        return command;
    }

    @Override
    public CommandMap getCommandMap() {
        CommandMap commandMap = null;
        try {
            if (Bukkit.getPluginManager() instanceof SimplePluginManager) {
                Field f = SimplePluginManager.class.getDeclaredField("commandMap");
                f.setAccessible(true);

                commandMap = (CommandMap) f.get(Bukkit.getPluginManager());
            }
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return commandMap;
    }
}
