package me.xcodiq.crypticshards.utilities.database.mysql;

import com.zaxxer.hikari.HikariDataSource;
import me.xcodiq.crypticshards.Core;
import me.xcodiq.crypticshards.utilities.database.DatabaseProvider;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class MySQL implements DatabaseProvider {

    private HikariDataSource hikari;

    @Override
    public void initialize(Core core) {
        core.getLogger().info("Initializing database... [MySQL]");
        ConfigurationSection databaseSection = core.getConfig().getConfigurationSection("database");
        if (databaseSection == null) {
            throw new IllegalStateException("MySQL not configured correctly. Cannot continue.");
        }

        hikari = new HikariDataSource();
        hikari.setMaximumPoolSize(databaseSection.getInt("pool-size"));

        hikari.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");

        hikari.addDataSourceProperty("serverName", databaseSection.getString("host"));
        hikari.addDataSourceProperty("port", databaseSection.getInt("port"));
        hikari.addDataSourceProperty("databaseName", databaseSection.getString("database"));

        hikari.addDataSourceProperty("user", databaseSection.getString("username"));
        hikari.addDataSourceProperty("password", databaseSection.getString("password"));

        hikari.addDataSourceProperty("characterEncoding", "utf8");
        hikari.addDataSourceProperty("useUnicode", "true");

        hikari.validate();
    }

    @Override
    public void deinitialize(Core core) {
        core.getLogger().info("De-initializing database... [MySQL]");
        hikari.close();
    }

    @Override
    public boolean existUser(String uuid) {
        try {
            ResultSet rs = executeQuery(Query.EXIST_CHECK, uuid);
            while (Objects.requireNonNull(rs).next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void insertUser(String uuid, int shards) {
        Player player = Bukkit.getPlayer(uuid);
        Core.newChain().async(() -> execute(Query.INSERT_USER, uuid, player.getName(), shards)).execute((exception, task) -> {
            if (exception != null) exception.printStackTrace();
        });
    }

    @Override
    public int getShards(String uuid) {
        try {
            ResultSet rs = executeQuery(Query.GET_SHARDS, uuid);
            while (Objects.requireNonNull(rs).next()) {
                return rs.getInt("shards");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void resetShards(String uuid) {
        Core.newChain().async(() -> execute(Query.UPDATE_SHARDS, uuid, 0)).execute((exception, task) -> {
            if (exception != null) exception.printStackTrace();
        });
    }

    @Override
    public boolean hasShards(String uuid, int shards) {
        try {
            ResultSet rs = executeQuery(Query.GET_SHARDS, uuid);
            while (Objects.requireNonNull(rs).next()) {
                if (rs.getInt("shards") >= shards) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void setShards(String uuid, int shards) {
        Core.newChain().async(() -> execute(Query.UPDATE_SHARDS, shards, uuid)).execute((exception, task) -> {
            if (exception != null) exception.printStackTrace();
        });
    }

    @Override
    public void addShards(String uuid, int shards) {
        int amount = this.getShards(uuid) + shards;
        Core.newChain().async(() -> execute(Query.UPDATE_SHARDS, amount, uuid)).execute((exception, task) -> {
            if (exception != null) exception.printStackTrace();
        });
    }

    @Override
    public void removeShards(String uuid, int shards) {
        int amount = this.getShards(uuid) - shards;
        Core.newChain().async(() -> execute(Query.UPDATE_SHARDS, amount, uuid)).execute((exception, task) -> {
            if (exception != null) exception.printStackTrace();
        });
    }

    @Override
    public Map<String, Integer> getTopShards() {
        Map<String, Integer> topTep = new LinkedHashMap<>();
        try {
            ResultSet rs = executeQuery(Query.GET_TOP_SHARDS);
            while (Objects.requireNonNull(rs).next()) {
                topTep.put(rs.getString("uuid"), Integer.valueOf(rs.getString("shards")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return topTep;
    }

    private void execute(String query, Object... parameters) {

        try (Connection connection = hikari
                .getConnection(); PreparedStatement statement = connection
                .prepareStatement(query)) {

            if (parameters != null) {
                for (int i = 0; i < parameters.length; i++) {
                    statement.setObject(i + 1, parameters[i]);
                }
            }

            statement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private ResultSet executeQuery(String query, Object... parameters) {
        try (Connection connection = hikari
                .getConnection(); PreparedStatement statement = connection
                .prepareStatement(query)) {
            if (parameters != null) {
                for (int i = 0; i < parameters.length; i++) {
                    statement.setObject(i + 1, parameters[i]);
                }
            }

            CachedRowSet resultCached = RowSetProvider.newFactory().createCachedRowSet();
            ResultSet resultSet = statement.executeQuery();

            resultCached.populate(resultSet);
            resultSet.close();

            return resultCached;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
