package me.xcodiq.crypticshards.utilities;

import me.xcodiq.crypticshards.Core;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FileUtils {

    private File file;
    private YamlConfiguration fileConfig;

    public FileUtils(File file) {
        this.file = file;
        this.fileConfig = YamlConfiguration.loadConfiguration(file);
    }

    public void setup() {
        Core.getInstance().saveResource(file.getName(), false);
        loadFile();
    }

    public void loadFile() {
        this.fileConfig = YamlConfiguration.loadConfiguration(file);
    }

    public void saveFile() {
        try {
            getFileConfig().save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public YamlConfiguration getFileConfig() {
        return fileConfig;
    }

    public void setFileConfig(YamlConfiguration fileConfig) {
        this.fileConfig = fileConfig;
    }
}
