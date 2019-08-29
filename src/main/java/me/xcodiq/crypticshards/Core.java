package me.xcodiq.crypticshards;

import co.aikar.taskchain.TaskChain;
import co.aikar.taskchain.TaskChainFactory;
import me.xcodiq.crypticshards.commands.base.CommandHandler;
import me.xcodiq.crypticshards.shard.ShardLoader;
import me.xcodiq.crypticshards.utilities.ChatUtils;
import me.xcodiq.crypticshards.utilities.PlaceholderAPI;
import me.xcodiq.crypticshards.utilities.database.DatabaseProvider;
import me.xcodiq.crypticshards.utilities.database.YAML;
import me.xcodiq.crypticshards.utilities.database.mysql.MySQL;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Core extends JavaPlugin {

    private static Core instance = null;
    private static Economy econ = null;
    private static TaskChainFactory taskChainFactory = null;
    private CommandHandler commandHandler = null;
    private FileManager fileManager = null;
    private File playerCache = new File(this.getDataFolder() + File.separator + "data", "playerCache.yml");
    public YamlConfiguration playerCacheConfig = YamlConfiguration.loadConfiguration(this.playerCache);
    private DatabaseProvider database;

    public Core() throws IllegalStateException {
        if (instance != null)
            throw new IllegalStateException("Only one instance can run");
        instance = this;
    }

    /**
     * Get a new TaskChain
     *
     * @return new taskChainFactory
     */
    public static <T> TaskChain<T> newChain() {
        return taskChainFactory.newChain();
    }

    /**
     * @return An instance of main class extending {@link JavaPlugin}
     */
    public static Core getInstance() {
        if (instance == null)
            throw new IllegalStateException("Cannot get instance: instance is null");
        return instance;

    }

    public DatabaseProvider getShardDatabase() {
        return database;
    }

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        this.saveDefaultConfig();

        fileManager = new FileManager();
        fileManager.setup();

        setDatabaseType();

        new Initializer(this);

        if (!setupEconomy()) {
            this.getLogger().severe("Cannot function due to no Vault dependency found!");
        }

        if (checkPAPI()) {
            new PlaceholderAPI().register();
            this.getLogger().info("PlaceholderAPI found, hooking...");
        }

        commandHandler = new CommandHandler("shards", null, null,
                "The main command for this plugin", new String[]{"shard", "crypticshard", "crypticshards"});
        commandHandler.enable(this);

        ShardLoader shardLoader = ShardLoader.getInstance();
        shardLoader.register(this);

        ShardTask.startTask(this);

        this.getServer().getConsoleSender()
                .sendMessage(ChatUtils.format("&2&l[!!] &2Thanks for using &a" + getDescription().getFullName()
                        + " &2(took " + (System.currentTimeMillis() - start) + "ms)"));
    }

    @Override
    public void onDisable() {
        commandHandler.disable(this);
        this.saveConfig();
        this.savePlayerCache();
    }

    /**
     * Save playerCache.yml file
     */
    public void savePlayerCache() {
        try {
            playerCacheConfig.save(playerCache);
        } catch (IOException e) {
            this.getLogger().warning("Could not save PlayerCache file!");
            e.printStackTrace();
        }
    }

    private boolean setupEconomy() {
        if (this.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = this.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private void setDatabaseType() {
        switch (this.getConfig().getString("database.type").toLowerCase()) {
            case "mysql":
                database = new MySQL();
                break;
            case "yaml":
            default:
                database = new YAML();
                break;
        }
        database.initialize(this);
    }

    public Economy getEconomy() {
        return econ;
    }

    public FileConfiguration getShopFile() {
        return fileManager.getShopFile();
    }

    private boolean checkPAPI() {
        return Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
    }
}
