package Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConfig {

    private Connection connection;

    private final String dbUrl = "jdbc:mysql://database-1.c4l7oml7jvpf.ap-south-1.rds.amazonaws.com:3306/mac_dev";
    private final String userName = "admin";
    private final String password = "12345678";

    public DbConfig() throws SQLException {
        this.connection = DriverManager.getConnection(this.dbUrl, this.userName, this.password);
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() throws SQLException {
        this.connection.close();
    }

}
