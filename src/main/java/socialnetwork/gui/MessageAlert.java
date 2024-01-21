package socialnetwork.gui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.util.Optional;

public class MessageAlert {
    public static void showMessage(Stage owner, Alert.AlertType type, String header, String text){
        Alert message=new Alert(type);
        message.setHeaderText(header);
        message.setContentText(text);
        message.initOwner(owner);
        message.showAndWait();
    }

    public static void showErrorMessage(Stage owner, String text){
        Alert message=new Alert(Alert.AlertType.ERROR);
        message.initOwner(owner);
        message.setTitle("Mesaj eroare");
        message.setContentText(text);
        message.showAndWait();
    }

    public static boolean showConfirmationDialog(Stage owner, Alert.AlertType type, String header, String text){
        Alert message = new Alert(type);
        message.setTitle("Confirmation Dialog");
        message.setHeaderText(header);
        message.setContentText(text);
        message.initOwner(owner);
        Optional<ButtonType> result = message.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}

