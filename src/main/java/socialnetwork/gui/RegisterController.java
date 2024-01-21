package socialnetwork.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import socialnetwork.domain.User;
import socialnetwork.service.MasterService;
import socialnetwork.utils.statics.Statics;

public class RegisterController implements Window {

    @FXML private TextField textFieldLastName;

    @FXML private TextField textFieldFirstName;

    @FXML private PasswordField passwordField;

    @FXML private TextField textFieldEmail;

    @FXML private TextField textFieldUsername;

    private MasterService service;

    private Stage stage;

    @FXML private void initialize() {

    }

    @Override
    public void setData(MasterService service, Stage stage) {
        this.service = service;
        this.stage = stage;
    }

    @FXML
    private void handleRegister() {
        String firstName = textFieldFirstName.getText();
        String lastName = textFieldLastName.getText();
        String username = textFieldUsername.getText();
        String email = textFieldEmail.getText();
        String password = passwordField.getText();

        User user = new User(firstName, lastName, username, email, password);
        saveUser(user);
    }

    private void saveUser(User user) {
        try {
            var rez = this.service.addUser(user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail(), user.getPassword());
            if (rez.isEmpty()) {
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Add user", "Account created successfully!");
            }
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
        stage.close();
    }

    private void clearFields() {
        textFieldLastName.setText("");
        textFieldFirstName.setText("");
        textFieldUsername.setText("");
        textFieldEmail.setText("");
        passwordField.setText("");
    }

    @Override
    public void show() {
        this.stage.setWidth(Statics.registerWindowWidth);
        this.stage.setHeight(Statics.registerWindowHeight);
        this.stage.centerOnScreen();
    }

    @Override
    public void hide() {
        clearFields();
    }

    public void handleBackToLogin(ActionEvent actionEvent) {
        WindowManager.getInstance().hideRegisterWindow();
        WindowManager.getInstance().showLoginWindow();
    }
}
