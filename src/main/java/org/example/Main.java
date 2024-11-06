package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:h2:~/testdb";
        String user = "sa";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement()) {

            statement.execute("DROP TABLE IF EXISTS test");
            System.out.println("Таблица 'test' успешно удалена.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
