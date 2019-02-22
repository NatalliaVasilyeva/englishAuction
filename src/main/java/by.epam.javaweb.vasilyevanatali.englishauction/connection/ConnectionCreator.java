package main.java.by.epam.javaweb.vasilyevanatali.englishauction.connection;

import main.java.by.epam.javaweb.vasilyevanatali.englishauction.connection.exception.ConnectionPoolException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ConnectionCreator {
    private static final Logger LOGGER = LogManager.getLogger(ConnectionCreator.class);
    private static final String DATABASE_PROPERTY = "database.properties";
    private static final String DATABASE_URL = "url";
    private static final String DATABASE_USER = "user";
    private static final String DATABASE_PASSWORD = "password";
    private static ResourceBundle resource = ResourceBundle.getBundle(DATABASE_PROPERTY);

    public static Connection createConnection() throws ClassNotFoundException, SQLException {

                    String url = resource.getString(DATABASE_URL);
                    String user = resource.getString(DATABASE_USER);
                    String password = resource.getString(DATABASE_PASSWORD);
                    Class.forName(resource.getString("driver"));
                    return DriverManager.getConnection(url, user, password);

    }

    public static int getPollSize(){
        return Integer.parseInt(resource.getString("poolSize"));
    }
}
