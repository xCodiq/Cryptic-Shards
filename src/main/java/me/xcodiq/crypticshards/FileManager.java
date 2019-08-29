package me.xcodiq.crypticshards;

import me.xcodiq.crypticshards.utilities.FileUtils;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class FileManager {

    private FileUtils shop = new FileUtils(new File(Core.getInstance().getDataFolder(), "shop.yml"));

    public void setup() {
        shop.setup();
    }

    public FileConfiguration getShopFile() {
        return shop.getFileConfig();
    }
}
