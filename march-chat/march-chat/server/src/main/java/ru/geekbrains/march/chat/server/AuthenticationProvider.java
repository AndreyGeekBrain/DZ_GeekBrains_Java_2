package ru.geekbrains.march.chat.server;

import java.sql.SQLException;

public interface AuthenticationProvider {
    String getNicknameByLoginAndPassword(String login, String password);
    void changeNickname(String oldNickname, String newNickname);
    void connectSQLLite();
    void CreateTable()throws SQLException;
}
