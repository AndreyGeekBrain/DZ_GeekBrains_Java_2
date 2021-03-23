package ru.geekbrains.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private List<ClientHandler> clients; // Список всех клиентов
    private int port; // Номер порта ТСР
    public Server(int port) throws Exception {
        this.port = port;
        this.clients = new ArrayList<>(); // Список, где хранятся все клиенты.
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            // try с ресурсами, с автоматическм закрытием сервера
            System.out.println("Сервер запущен на порту: "+ port+ " Ожидаю подключения клиента!");
            while (true) {
                // Безконечный цикл, который создает ожидание соединения (то есть ждет адрес ссылки на объект Socket,
                // который создает клиент (Client), для соединения с серверром.
                Socket socket = serverSocket.accept();
                System.out.println("Клиент подключился, какой молодец!!!");
// Создание объекта ClientHandler, говорит нам, отом что мы создаем клиента он работает,
// но никаких сообщений не получает. В новый объект в параметры передаем ссылку на сам сервер и на объект (socket) отвечающий за соединение.
                new ClientHandler(this , socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void subscribe(ClientHandler clientHandler){
        // метод отвечающий за добавление новоно клиента-соединения в список
        clients.add(clientHandler);
    }
    public void unsubscribe(ClientHandler clientHandler){
        // метод отвечающий за исключение клиента из списка рассылки
        clients.remove(clientHandler);
    }
    public void brodCastMessage(String message) throws Exception {
        // Метод отвечающий за широковещательную рассылку от одного клиента всем кто всписке arraylist
        for (ClientHandler clientHandler: clients) {
            clientHandler.sendMessage(message);
        }
    }
}
