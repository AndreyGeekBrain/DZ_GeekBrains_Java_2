package AppMainDZ_4;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    // Stage - это окна в JavaFX. Далее primaryStage. мы обращаемся к окну.
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("chat.fxml"));
        // из файла chat.fxml.fxml загружаем наш интерфейс, то как он выглядит
        primaryStage.setTitle("Super Chat");
        //Scene - это внутренне наполнение окна
        primaryStage.setScene(new Scene(root, 400, 300));
        //primaryStage - это аналог setvisible из javaswing делает видимым.
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
