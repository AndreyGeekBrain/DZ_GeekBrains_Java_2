package ru.geekbrains.march.chat.server;

import java.sql.SQLException;

public class ServerApp {
    public static void main(String[] args) throws SQLException {
        new Server(8189);
    }
}
