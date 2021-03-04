package AppMainDZ_4;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

// Controller - служит для управления.
public class  Controller {

@FXML
TextArea chatWindows;

@FXML
TextField mainTextField;

@FXML
Button chatButton;

    public void truChat(ActionEvent actionEvent) {
        chatWindows.appendText(mainTextField.getText()+"\n");
        mainTextField.clear();
    }
}
