package org.example.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.experimental.UtilityClass;
import org.flywaydb.core.Flyway;

import java.sql.Connection;
import java.sql.SQLException;

@UtilityClass
public class Database {
    private static final String DB_URL = System.getProperty("test.db", "jdbc:h2:~/testdb");
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";
    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

    static {
        config.setJdbcUrl(DB_URL);
        config.setUsername(DB_USER);
        config.setPassword(DB_PASSWORD);
        ds = new HikariDataSource(config);
        Flyway.configure()
                .dataSource(ds)
                .baselineOnMigrate(true)
                .baselineVersion("1")
                .locations("classpath:db/migrations")
                .load()
                .migrate();
    }

    public static Connection getConnetction() throws SQLException {
        System.out.println("Connecting to database with URL: " + DB_URL);
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
