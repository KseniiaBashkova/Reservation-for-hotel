package Help;

import Help.Exception.DatabaseConnectionException;
import Help.Exception.NotFoundConfigurationException;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Represents class to connection with database.
 */
public class DatabaseConnection {

    private final static Logger logger = Logger.getLogger(DatabaseConnection.class);

    // Use singleton pattern.
    private static Connection singleInstance;

    /**
     * Get single instance of db connection.
     * @return Connection to db.
     */
    public static Connection getConnection() {

        if (singleInstance == null) {
            Properties properties = new Properties();
            Config config = Config.getConfig(properties);
            try {
                singleInstance = DriverManager.getConnection(config.url, config.user, config.password);
                System.out.println("Connected to the PostgreSQL server successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new DatabaseConnectionException("Error during create connection with db\n Error: " + e.getMessage());
            }
        }

        return singleInstance;
    }
}