package ru.geekbrains.chat.client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
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

public class Controller {

    @FXML
    TextField msgField, usernameField;

    @FXML
    TextArea msgArea;

    @FXML
    HBox loginPanel, msgPanel;

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String username;

    public void setUsername(String username){
        if (username != null){
            // Тогда loginPanel мы скрываем, то есть пользователь ввел свой НИК(логин) и он принят сервером
            // Сетевой чат продолжает свою работу и переходим к полю сообщений.
            loginPanel.setVisible(false);
            // Выделяем место под поле текстовое сообщениен, то есть убираем ранее выставленный ограничитель.
            loginPanel.setManaged(false);
            // А панель с сообшениями должны показать.
            msgPanel.setVisible(true);
            msgPanel.setManaged(true);
        }else {
            // Тут описывается процесс то, как мы выходим из сети (то есть username сброшен), то есть все,
            // что было ранее возвращается, то есть появляется панель авторизации.
            loginPanel.setVisible(true);
            loginPanel.setManaged(true);
            msgPanel.setVisible(false);
            msgPanel.setManaged(false);
        }
    }

    public void sendMsg()  {
        try {
            out.writeUTF(msgField.getText()); // Из крайнего нижнего поля введенный пользователем текст отправояет
            // в выходящий поток out.
            msgField.clear(); // После отправки очищает поле от введенной информации
            msgField.requestFocus(); // Подсвечивает фокус поля текст
        } catch (IOException e) {
            //Выскакивает сообщение, о том, что пустое сообщение нет сиволов.
            Alert alert = new Alert(Alert.AlertType.ERROR,"НЕ возможно отправить сообщение!!!", ButtonType.OK);
            alert.showAndWait();
        }
    }
    // Метод connect нужен так как у нас появился логин (теперь сначало логин, потом инициализация)
    // Ранее код в методе был прописан интерфейсом Inicialization, то есть подключение шло автоматически.
    public void connect(){
        // При старте мы подключимя к серверу
        try {
            socket = new Socket("localhost",8289);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            Thread thread = new Thread(() -> {
                try {
                    while (true){
                        String str = in.readUTF();
                        // Если во входящем потоке сообщение начинается со слеша,
                        // то сообщение служебное и мы попадем в первый if, второй if("\loin")
                        // говорит что проводим авторизацию.
                        if (str.startsWith("/ ")) {
                            // client -> server: /login bob - комманда для логина
                            // server -> client: /login_ok bob - сервер в ответ отправил
                            if (str.startsWith("/login_ok ")) {
            // Запускаем описанный выше метод username добавляя в него часть строки;
            // Запись (msg.split("\\s")[1]) - говорит нам, что у стороки полученной
            // от вервера мы отсекаем сплитом все лишнее по по пробелу и берем вторую часть (0 - первая).
                                setUsername(str.split("\\s")[1]);
                            }
                            continue;
                        }
                        //Если ("/") НЕТ, то мы отправляем сообщение в окно.
                        msgArea.appendText(str + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            thread.start();

        } catch (Exception e) {
            System.out.println("Хост 8289 не найден!");
            e.printStackTrace();
        }finally {
            // Когда мы поработали, то должны закрыть соединение и разлогиниться.
            disconnect();
        }
    }
    // При нажатии кнопки "логин" мы должны открыть соединение с сервером, что происходит:
    public void login() {
        // Первый if проверяет наличие соедиения, если оно еще не установлено, то запускает метод connect()
        if (socket == null || socket.isClosed()){
            connect();
        }

        try {
            // Далее во выходящий поток out передаем сообщение из текстового поля с Ником (usernameField)
            out.writeUTF("/login "+ usernameField.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect(){
        // Разлогинились закрываем соединенте
        setUsername(null);

        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
