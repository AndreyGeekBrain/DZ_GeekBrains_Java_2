package ru.geekbrains.march.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private int port;
    // Создаем список клиентов, то есть храним в листе все клиентские обработчики.
    private List<ClientHandler> clients;

    public Server(int port){
        this.port = port;
        this.clients = new ArrayList<>();
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Ждем подключение клиента к серверу на порт 8189!!!");
            while (true) {
                // Данный цикл обеспечивает возможность подключения множеству клиентов, без него
                // поток main пошелбы по строчно далее выполнять программу и завершилбы ее, не давбольше не кому подключится.
                Socket socket = serverSocket.accept();
                // К нам клиент подключился и мы отдаем его ClientHandler
                new ClientHandler(socket,this);
                // В ClientHandler создается отдельный поток для постоянного общения с клиентом (while)
                System.out.println("Клиент подключился !!!");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    // Подписываем клиента в список адресатов
    public void subscribe (ClientHandler clientHandler){
        clients.add(clientHandler);
    }

    public void unsubscribe (ClientHandler clientHandler){
        clients.remove(clientHandler);
    }

    public void broadcastMessage(String message) throws IOException {
        for (ClientHandler clientHandler :clients){
            clientHandler.sendMessage(message);
        }
    }

    public void unicastMessage(String name, String message) throws IOException {
        for (ClientHandler clientHandler: clients) {
            if (clientHandler.getUsername().equals(name)){
                System.out.println("if");
                clientHandler.sendMessage(message);
            }
        }
    }

    public boolean isNicBusy(String username){
        for (ClientHandler clientHandler:clients) {
            if (clientHandler.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
}