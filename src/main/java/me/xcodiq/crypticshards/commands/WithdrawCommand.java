package me.xcodiq.crypticshards.commands;

import me.xcodiq.crypticshards.commands.base.CommandBase;
import me.xcodiq.crypticshards.conversations.WithdrawConv;
import me.xcodiq.crypticshards.conversations.factory.ShardConversation;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WithdrawCommand extends CommandBase {

    public WithdrawCommand() {
        super("withdraw", "crypticshards.command.withdraw", "Withdraw shards by typing an amount",
                new String[]{"withdrawshards"}, 0, 0);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                new ShardConversation(player, new WithdrawConv(player), "cancel").start();
            }
        }
    }
}
