package ru.geekbrains.march.chat.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller  {
    private DataInputStream in;
    private DataOutputStream out;
    private String username;
    private Socket socket; //Ссылка на объект "сетевое соединение" открывает соединение с сервером

    @FXML
    HBox loginPanel,msgPanel;
    @FXML
    TextArea msgArea;
    @FXML
    TextField msgField,usernameField;

    public void setUsername(String username){
        this.username = username;

        if (username != null) {
            loginPanel.setVisible(false);
            loginPanel.setManaged(false);
            msgPanel.setVisible(true);
            msgPanel.setManaged(true);
        }else {
            loginPanel.setVisible(true);
            loginPanel.setManaged(true);
            msgPanel.setVisible(false);
            msgPanel.setManaged(false);
        }
    }

    public void login() {

        if (socket == null || socket.isClosed()) {
            connect();
        }
        // Проверка на пустую строку в поле логин и попытку его отправить.
        if (usernameField.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Имя не может быть пустым", ButtonType.OK);
            alert.showAndWait();
            return; // return рерывает метод login, таое возможно когда метод со значением void
        }
        // Послето того, как соединение произойдет, то мы в иходящий поток out высылаем сдедующее сообщение
        // состоящие из двух частей (служебная часть + НИК user) "/login" + usernameField.getText().
        try {
            out.writeUTF("/login " + usernameField.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connect(){
        try {
            socket = new Socket("localhost", 8189);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // Здесь слушаем сообщения от сервера
                        // Цикл авторизации
                        while (true) {
                            String msg = in.readUTF();
//                            if (msg.startsWith("/")) {
//                                // Логика у if выше следующая, что если сообщение начинается со ("/"),то служебное и
//                                // оно обрабатывается по логике ниже:
//                                // client -> server: /login bob
//                                // server -> client: /login_ok bob
                            if (msg.startsWith("/login_ok ")) {
                                setUsername(msg.split("\\s")[1]);
                                // Если от сервера пришло сообщение, что логин принят,
                                // то "панель Логин" скрываем, а панель "сообщений" открываем.
                                break;
                            }
                            //continue; // ""Прерывает"" дальнейшее выполнение программы и СНОВА (ЗАНОВО) начинает итерацию while().
                            if (msg.startsWith("/login_failed ")) {
                                String cause = msg.split("\\s", 2)[1];
                                msgArea.appendText(cause + "\n");
                            }
                        }

                        while (true){
                            String msg = in.readUTF();
                            msgArea.appendText(msg); // Если сообщение не служебное, то добавляем его в окно msgArea
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        disconnect();
                    }
                }
            });
            t.start();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Не возможно присоединится к серверу!!!", ButtonType.OK);
            alert.showAndWait();
        }
    }

//    @Override
//    public void initialize(URL location, ResourceBundle resources) {
//    //Итерфейс Initializable выполняет преднастройку контроллера, где и открывает сетевое соединение.
//        // Логика метода initialize - это по сути  автоматический запуск соединения с сервером при запуске клиента.
//    }

    // Логика метода connect - это по сути отказ от автоматического запуска соединения с сервером
    // при запуске клиента.

    public void sendMsg() {
        try {
            out.writeUTF(msgField.getText());
            // По id вызываем поле с тесктом и разбиваем его на байты: msgField.getText().getBytes()
            //Можно перекидываться байтами (.getBytes()), а можно строками в кодировки UTF-8
            // (DataoutputStream) out и метод out.writeUTF())
            msgField.clear();
            msgField.requestFocus(); // Взвращает выделенную рамку полю сообщения.
        }   catch (IOException e) {
            Alert.AlertType alertAlertType;
            Alert alert = new Alert(Alert.AlertType.ERROR, "Невозможно отправить сообщение!!!");
            alert.showAndWait();
        }

    }
    // Суть метода заключаестся в том, что бы при нажатии на кнопку (Button text="Войти"),
    // мы сначало проверили, что соединение (socket) ОТСУТСТВУЕТ!!! А потом выполняли соединение с сервером по средством метода connect.

    public void disconnect(){
        setUsername(null);
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
