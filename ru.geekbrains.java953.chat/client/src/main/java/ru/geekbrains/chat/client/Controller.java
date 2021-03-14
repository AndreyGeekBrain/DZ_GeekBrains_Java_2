package ru.geekbrains.chat.client;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    TextField msgField;

    @FXML
    TextArea msgArea;

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // При старте мы подключимя к серверу
        try {
            socket = new Socket("localhost",8189);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            Thread thread = new Thread(() -> {
                try {
                    while (true){
                        String str = in.readUTF();
                        msgArea.appendText(str + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            thread.start();

        } catch (Exception e) {
            System.out.println("Хост 8189 не найден!");
            e.printStackTrace();
        }
    }
    public void sendMsg()  {
        try {
            out.writeUTF(msgField.getText());
            msgField.clear();
        } catch (IOException e) {
            //Выскакивает сообщение, о том, что пустое сообщение нет сиволов.
            Alert alert = new Alert(Alert.AlertType.ERROR,"НЕ возможно отправить сообщение!!!", ButtonType.OK);
            alert.showAndWait();
        }
    }
}
