package me.xcodiq.crypticshards.commands;

import me.xcodiq.crypticshards.commands.base.CommandBase;
import me.xcodiq.crypticshards.gui.ShopGUI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShopCommand extends CommandBase {

    public ShopCommand() {
        super("shop", null, "Open the shard shop gui",
                null, 0, 0);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                ShopGUI gui = new ShopGUI();
                gui.openGUI(player);
            }
        }
    }
}
