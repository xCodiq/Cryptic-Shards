package me.xcodiq.crypticshards.utilities.database.mysql;

public class Query {

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `playerCache` (\n"
            + "    `uuid` VARCHAR(36)  NOT NULL ,\n"
            + "    `name` varchar(32)  NOT NULL ,\n"
            + "    `shards` int  DEFAULT 1 ,\n"
            + "    PRIMARY KEY (`uuid`), \n"
            + "    UNIQUE (`uuid`)\n"
            + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";

    public static final String INSERT_USER = "INSERT IGNORE INTO `playerCache` (uuid, name, shards) VALUES(?, ?, ?)";

    public static final String EXIST_CHECK = "SELECT uuid FROM `playerCache` WHERE uuid=?";

    public static final String GET_SHARDS = "SELECT `shards` FROM `playerCache` WHERE uuid=?";

    public static final String UPDATE_SHARDS = "UPDATE `playerCache` SET shards=? WHERE uuid=?";

    public static final String GET_TOP_SHARDS = "SELECT * FROM `playerCache` ORDER BY `shards` DESC LIMIT 10";
}
