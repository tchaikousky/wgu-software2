package sample.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/***
 * This class defines a Conn object which connects to an external database.
 * @author Tchaikousky Thomas
 */
public class Conn {
    private final String url; //the conn's url
    private final Properties properties; //the conn's properties
    private final String databaseName; //the conn's databaseName

    /***
     * This constructor creates a Conn object with the database fields needed for this project preset.
     */
    public Conn() {
        this.url = "jdbc:mysql://wgudb.ucertify.com/WJ03ST8";
        this.databaseName = "WJ03ST8";
        this.properties = new Properties();
        this.properties.setProperty("databaseName", "WJ03ST8");
        this.properties.setProperty("password", "53688069128");
        this.properties.setProperty("user", "U03ST8");
    }

    /***
     * This constructor creates a Conn object with the passed parameters setting the connection.
     * @param url The conn's url
     * @param databaseName The conn's databaseName
     * @param userName The conn's userName
     * @param password The conn's password
     */
    public Conn(String url, String databaseName, String userName, String password) {
        this.databaseName = databaseName;
        this.url = url + "/" + databaseName;
        this.properties = new Properties();
        this.properties.setProperty("databaseName", databaseName);
        this.properties.setProperty("user", userName);
        this.properties.setProperty("password", password);
    }

    /***
     * This method returns a Connection object from the passed Conn object url and properties fields.
     * @return Connection
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.url, this.properties);
    }
}
