package ru.geekbrains.chat.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MaimAppServer {
    static int count;
    public static void main(String[] args) throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(8189)) {
            System.out.println("Сервер запущен на порту 8189. Ожидаю подключения клиента!");
            Socket socket = serverSocket.accept();
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            System.out.println("Клиент подключился, какой молодец!!!");

            String str;
            while (true) {
                str=in.readUTF();
                System.out.println(str);
                count++;
                if(str.equals("/stat")){
                    out.writeUTF("Вами было отправлено: " +count+" сообщение(ий)");
                }else{out.writeUTF(str);}
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}






