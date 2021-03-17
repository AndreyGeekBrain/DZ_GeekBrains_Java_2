package ru.geekbrains.chat.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MaimAppServer {

    public static void main(String[] args) throws Exception {
        // В данном методе просто запускаем сервер.
        new Server(8289);
    }
}






