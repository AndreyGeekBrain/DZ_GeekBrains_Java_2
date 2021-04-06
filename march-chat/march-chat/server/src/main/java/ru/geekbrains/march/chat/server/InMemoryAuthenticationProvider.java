package ru.geekbrains.march.chat.server;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InMemoryAuthenticationProvider implements AuthenticationProvider {
    private static Connection connection;
    private static Statement stmt;
    private static PreparedStatement psInsert;

    private class User {
        private String login;
        private String password;
        private String nickname;

        public User(String login, String password, String nickname) {
            this.login = login;
            this.password = password;
            this.nickname = nickname;
        }
    }

    public InMemoryAuthenticationProvider() {
        //Создаем базу, таблицу,дефолтные User
        connectSQLLite();
        CreateTable();
        try {
            defoltUsers();
        } catch (SQLException e) {
            System.out.println("Дефолтные значения НЕ подгруженны !!!");
            disconnect();
        }
    }

    //Дефорлтное заполнение БД - заготовка
    public void defoltUsers() throws SQLException {
        stmt.executeUpdate("INSERT INTO nicktable (login,password,nickname) VALUES ('Bob','100','MegaBob');");
        stmt.executeUpdate("INSERT INTO nicktable (login,password,nickname) VALUES ('Jack','100','Mystic');");
        stmt.executeUpdate("INSERT INTO nicktable (login,password,nickname) VALUES ('John','100','Wizard');");
        System.out.println("Дефолтные значения подгруженны !!!");
    }

    //Создание таблицы
    @Override
    public void CreateTable() {
        try {
            stmt.executeUpdate("CREATE TABLE nicktable (\n" +
                    "    id       INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "    login    TEXT,\n" +
                    "    password TEXT,\n" +
                    "    nickname TEXT\n" +
                    ");");
            System.out.println("Таблица создана!!!");
        } catch (SQLException e) {
            System.out.println("Не возможно создать таблицу в БД");
        }
    }

    // Создание БД, загрузка драйвера, создание соединения.
    @Override
    public void connectSQLLite() {
        try {
            Class.forName("org.sqlite.JDBC"); // Для использования драйвера используем его.
            connection = DriverManager.getConnection("jdbc:sqlite:databaseChat.db");
            // загружает класс в память и этим гарантирует выполнение статического блока инициализации, а значит и регистрацию драйверв в DriverManager.
            stmt = connection.createStatement(); // Предоставляет доступ (организовывает соединение) с БД.
            System.out.println("БД создана");
        } catch (ClassNotFoundException | SQLException e) {
            //БД не открывается то смысла в дальнейшей работе нет.
            throw new RuntimeException("Невозможно подключится к БД");
        }
    }

    //
    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
        System.out.println(login +" "+ password);
        try (ResultSet rs = stmt.executeQuery("select * from nicktable where login = '"+ login +"' , password = '"+ password +"';");) {
            System.out.println(rs.getString(4));
            return rs.getString(4);
        } catch (SQLException e) {
            System.out.println("Значение не найдено!");
            disconnect();
        }
        return null;
    }

    // Изменяем значение nickname
    @Override
    public void changeNickname(String oldNickname, String newNickname) {
        try (ResultSet rs = stmt.executeQuery("select id from nicktable where nickname = ' " + oldNickname + "';");) {
            int oldNicknameID = rs.getInt(1);
            stmt.executeUpdate("update nicktable set nickname = ' "+ newNickname +"'  where id = ' " + oldNicknameID + "';");

        } catch (SQLException e) {
            System.out.println("Значение не найдено!");
            disconnect();
        }
    }

    public static void disconnect() {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (psInsert != null) {
                psInsert.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

}
