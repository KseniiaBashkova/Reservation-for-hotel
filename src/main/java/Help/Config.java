package Help;

import Help.Exception.NotFoundConfigurationException;
import org.apache.log4j.Logger;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Represents class to get Configuration to connect with db.
 */
public class Config {

    // Use singleton pattern.
    private static Config singleInstance;
    private InputStream inputStream;
    private Logger logger = Logger.getLogger(Config.class);


    public String url;
    public String user;
    public String password;
    public int port;

    public static Config getConfig(Properties properties) {

        if (singleInstance == null) {
            singleInstance = new Config();
            singleInstance.setConfig(properties);
        }

        return singleInstance;
    }

    /**
     * Sets configuration from config.properties file.
     * @param properties Properties class.
     */
    private void setConfig(Properties properties) {
        try {
            String propFileName = "config.properties";

            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new NotFoundConfigurationException("Property file '" + propFileName + "' not found in the classpath.");
            }

            url = properties.getProperty("url");
            user = properties.getProperty("user");
            password = properties.getProperty("password");
            port = Integer.parseInt(properties.getProperty("port"));
            inputStream.close();

        } catch (Exception e) {
            logger.error(e.getMessage());
            System.out.println("Exception: " + e);
        }
    }

    public String toString() {
        return "Password: " + password + "\nUrl: " + url + "\nUser: " + user + "\nPort: " + port + "\n";
    }
}
