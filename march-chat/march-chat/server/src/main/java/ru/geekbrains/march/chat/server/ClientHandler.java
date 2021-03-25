package ru.geekbrains.march.chat.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private Server server; // Обработчик создается объектом сервер и мы помимо порта передаем обработчику ссылку на сервер
    // Для того, что бы мы могли воспользоваться методами объекта сервер unsubscribe & broadcastMessage
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String username;

    public String getUsername() {
        return username;
    }

    public ClientHandler(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());

        Thread t = new Thread(() -> {
                try {
                    // Цикл авторизации.
                    while (true) {
                        String msg = in.readUTF();
                        if (msg.startsWith("/login ")) { // Если сообщение начинается с "/login",то мы получаем имя пользователя.
                            String usernameFromLogin = msg.split("\\s")[1];
                            if (server.isNicBusy(usernameFromLogin)){
                                sendMessage("/login_failed Current nickname is already used");
                                continue;
                            }

                            username = usernameFromLogin;  //msg.split("\\s")[1]; // "\\s" - пробел; [1] - вторая часть массива.
                            sendMessage("/login_ok " + username); // Сообщение, что ты успешно залогинен.
                            server.subscribe(this); // Добавляем обработчик в лист, тем самым создаем список клиентов.
                            break;
                        }
                    }
                    // Цикл общения с клиентом
                    while (true) {
                        String msg = in.readUTF();
                        if (msg.equals("/who_am_i")){
                            sendMessage(this.getUsername() + " это твой ник!" +"\n");
                            continue;
                        }

                        if (msg.startsWith("/exit")) {
                            this.disconnect();
                        }

                        if (msg.startsWith("/w ")) {
                            String nameSplit = msg.split("\\s",3)[1];
                            String massageSplit = msg.split("\\s",3)[2];
                            server.unicastMessage(nameSplit,massageSplit);
                        } else {
                            server.broadcastMessage(username + ": " + msg+"\n"); // Благодаря добавлению ссылки на сервер в ClientHandler
                            // Мы используем метод сервера по широковещательной рассылки.
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    disconnect();
                }
        });
        t.start();
    }

    public void sendMessage(String message) throws IOException {
        out.writeUTF(message);
    }

    public void disconnect() {

        server.unsubscribe(this);// Благодаря добавлению ссылки на сервер в ClientHandler
        // Мы используем метод сервера по удалению текущего обработчика из адресного списка.

        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
