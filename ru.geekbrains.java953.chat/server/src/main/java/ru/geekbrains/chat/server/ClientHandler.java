package ru.geekbrains.chat.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private String username;
    private Server server;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public ClientHandler(Server server, Socket socket) throws Exception {
        this.server = server;
        this.socket = socket;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());

        Thread t = new Thread(() ->{
            try {
                // Цикл авторизаци
            while (true) {
                String str = in.readUTF();
                // При первом получении сообщения мы проводим анализ если там есть "/login", то сообщение служебное
                // и мы к сообщению клиента добавляем ОК, тем самым говорим о том, что приняли логин клиента.
                if (str.startsWith("/login ")) {
                    username = str.split("\\s")[1];
                    // Отправляем в выходящий поток (out) сообщение
                    sendMessage("/login_ok " + username);
                    // Командой просим сервер приписать нас на рассылку
                    server.subscribe(this);
                    break;
                }
            }
                // Цикл общения с клиентом
                    while(true) {
                        String str = in.readUTF();
                        server.brodCastMessage( username + ": " + str);
                    }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                this.disconekt();
            }
        });
        t.start();
    }

    public void sendMessage(String message) throws Exception{
        out.writeUTF(message);
    }

    public void disconekt(){
        server.unsubscribe(this);
        if (socket != null){
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}


