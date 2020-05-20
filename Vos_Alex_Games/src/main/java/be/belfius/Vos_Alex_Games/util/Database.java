package be.belfius.Vos_Alex_Games.util;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
//    private Logger logger = LoggerFactory.getLogger(Database.class);

    public Connection createConnection() throws SQLException {
        try {
            Class.forName(Helper.loadPropertiesFile().getProperty("db.driver"));
        } catch (ClassNotFoundException e) {
//            logger.error("Error mysql driver:", e);
        }
        return DriverManager.getConnection(Helper.loadPropertiesFile().getProperty("db.url")
                , Helper.loadPropertiesFile().getProperty("db.username")
                , Helper.loadPropertiesFile().getProperty("db.password"));
    }
}

 