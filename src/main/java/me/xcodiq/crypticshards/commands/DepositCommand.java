package me.xcodiq.crypticshards.commands;

import me.xcodiq.crypticshards.commands.base.CommandBase;
import me.xcodiq.crypticshards.conversations.DepositConv;
import me.xcodiq.crypticshards.conversations.factory.ShardConversation;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DepositCommand extends CommandBase {

    public DepositCommand() {
        super("deposit", "crypticshards.command.deposit", "Deposit shards by typing an amount",
                new String[]{"depositshards"}, 0, 0);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                new ShardConversation(player, new DepositConv(player), "cancel").start();
            }
        }
    }
}