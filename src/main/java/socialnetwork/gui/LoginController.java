package socialnetwork.gui;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.service.MasterService;
import socialnetwork.utils.statics.Statics;

public class LoginController implements Window {

    public AnchorPane mainBackground;
    public AnchorPane smallerBackground;
    public AnchorPane widgetsLoginBackground;
    public Hyperlink forgotPasswordLink;
    public Hyperlink signUpLink;
    public TextField passwordTextFieldVisibleLogin;
    public ImageView openEyeImageLogin;
    public ImageView closedEyeImageLogin;
    public AnchorPane middleBackground;
    public AnchorPane widgetRegisterBackground;
    public Hyperlink backToLoginLink;
    public TextField emailTextFieldRegister;
    public TextField usernameTextFieldRegister;
    public PasswordField passwordTextFieldRegister;
    public ImageView openEyeImagePassRegister;
    public TextField passwordTextFieldVisibleRegister;
    public ImageView closedEyeImagePassRegister;
    public PasswordField confirmPasswordTextField;
    public ImageView openEyeImageConfRegister;
    public TextField confirmPasswordTextFieldVisibleRegister;
    public ImageView closedEyeImageConfRegister;
    private MasterService masterService;

    @FXML
    private TextField emailTextFieldLogin;

    @FXML
    private PasswordField passwordTextFieldLogin;

    @FXML
    private Label failedLoginLabel;

    private Stage stage;

    private boolean isRegisterVisible = false;

    @Override
    public void setData(MasterService service, Stage stage) {
        this.masterService = service;
        this.stage = stage;
//        this.masterService.addObserver(this);
    }

    @FXML
    private void handleLogin(ActionEvent actionEvent) {
        String email = emailTextFieldLogin.getText();
        String password;
        if (passwordTextFieldLogin.isVisible())
            password = passwordTextFieldLogin.getText();
        else
            password = passwordTextFieldVisibleLogin.getText();
        try {
            var oUser = this.masterService.tryLogin(email, password);
            boolean loginSuccessful = oUser.isPresent();
            if (loginSuccessful) {
                WindowManager.getInstance().hideLoginWindow();
                WindowManager.getInstance().showRegisterWindow(oUser.get());
            } else {
                failedLoginLabel.setVisible(true);
            }
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    @Override
    public void show() {
        this.stage.setWidth(Statics.loginWindowWidth);
        this.stage.setHeight(Statics.loginWindowHeight);

        this.stage.setY(0f);
        this.stage.setX(0f);

        enableLogin();
        disableRegister();
    }

    @Override
    public void hide() {
        disableRegister();
        disableLogin();
    }

    private void enableLogin() {
        this.widgetsLoginBackground.setVisible(true);
        this.passwordTextFieldLogin.setVisible(true);
        this.openEyeImageLogin.setVisible(true);
        this.passwordTextFieldVisibleLogin.setVisible(false);
        this.closedEyeImageLogin.setVisible(false);
        this.failedLoginLabel.setVisible(false);
        this.signUpLink.setVisited(false);

        this.isRegisterVisible = false;
    }

    private void disableLogin() {
        this.failedLoginLabel.setVisible(false);
        this.emailTextFieldLogin.clear();
        this.passwordTextFieldLogin.clear();
        this.passwordTextFieldVisibleLogin.clear();

        this.widgetsLoginBackground.setVisible(false);
    }

    private void enableRegister() {
        this.widgetRegisterBackground.setVisible(true);
        this.passwordTextFieldRegister.setVisible(true);
        this.openEyeImagePassRegister.setVisible(true);

        this.confirmPasswordTextField.setVisible(true);
        this.openEyeImageConfRegister.setVisible(true);

        this.passwordTextFieldVisibleRegister.setVisible(false);
        this.closedEyeImagePassRegister.setVisible(false);

        this.confirmPasswordTextFieldVisibleRegister.setVisible(false);
        this.closedEyeImageConfRegister.setVisible(false);

        this.backToLoginLink.setVisited(false);

        this.isRegisterVisible = true;
    }

    private void disableRegister() {
        this.usernameTextFieldRegister.clear();
        this.emailTextFieldRegister.clear();
        this.passwordTextFieldRegister.clear();
        this.passwordTextFieldVisibleRegister.clear();
        this.confirmPasswordTextField.clear();
        this.confirmPasswordTextFieldVisibleRegister.clear();
        clearTooltips();

        this.widgetRegisterBackground.setVisible(false);
    }

    private void clearTooltips() {
        this.usernameTextFieldRegister.setTooltip(null);
        this.emailTextFieldRegister.setTooltip(null);
        this.passwordTextFieldRegister.setTooltip(null);
        this.passwordTextFieldVisibleRegister.setTooltip(null);
        this.confirmPasswordTextField.setTooltip(null);
        this.confirmPasswordTextFieldVisibleRegister.setTooltip(null);
    }

//    @Override
//    public void update(Event event) {
//        AbstractEvent abstractEvent = (AbstractEvent) event;
//        if(abstractEvent.getEventType() == ChangeEventType.LOGIN)
//            show();
//    }


    @FXML
    private void handleShowRegister(ActionEvent actionEvent) {
        resetTextFieldColor();
        disableLogin();
        enableRegister();
    }

    public void handleShowPassword(MouseEvent mouseEvent) {
        if (!isRegisterVisible) {
            switchFieldsVisibility(this.passwordTextFieldVisibleLogin, this.passwordTextFieldLogin, this.closedEyeImageLogin, this.openEyeImageLogin);
            return;
        }
        switchFieldsVisibility(this.passwordTextFieldVisibleRegister, this.passwordTextFieldRegister, this.closedEyeImagePassRegister, this.openEyeImagePassRegister);
        switchFieldsVisibility(this.confirmPasswordTextFieldVisibleRegister, this.confirmPasswordTextField, this.closedEyeImageConfRegister, this.openEyeImageConfRegister);
        this.passwordTextFieldVisibleRegister.setStyle(this.passwordTextFieldRegister.getStyle());
        this.confirmPasswordTextFieldVisibleRegister.setStyle(this.confirmPasswordTextField.getStyle());
        this.passwordTextFieldVisibleRegister.setTooltip(this.passwordTextFieldRegister.getTooltip());
        this.confirmPasswordTextFieldVisibleRegister.setTooltip(this.confirmPasswordTextField.getTooltip());
    }

    private void switchFieldsVisibility(TextField passwordTextFieldToShow, TextField passwordTextFieldToHide,
                                        ImageView imageViewToShow, ImageView imageViewToHide) {
        String password = passwordTextFieldToHide.getText();
        passwordTextFieldToHide.setVisible(false);
        imageViewToHide.setVisible(false);

        passwordTextFieldToShow.setText(password);
        passwordTextFieldToShow.setVisible(true);
        imageViewToShow.setVisible(true);
    }

    public void handleHidePassword(MouseEvent mouseEvent) {
        if (!isRegisterVisible) {
            switchFieldsVisibility(this.passwordTextFieldLogin, this.passwordTextFieldVisibleLogin, this.openEyeImageLogin, this.closedEyeImageLogin);
            return;
        }
        switchFieldsVisibility(this.passwordTextFieldRegister, this.passwordTextFieldVisibleRegister, this.openEyeImagePassRegister, this.closedEyeImagePassRegister);
        switchFieldsVisibility(this.confirmPasswordTextField, this.confirmPasswordTextFieldVisibleRegister, this.openEyeImageConfRegister, this.closedEyeImageConfRegister);
        this.passwordTextFieldRegister.setStyle(this.passwordTextFieldVisibleRegister.getStyle());
        this.confirmPasswordTextField.setStyle(this.confirmPasswordTextFieldVisibleRegister.getStyle());
        this.passwordTextFieldRegister.setTooltip(this.passwordTextFieldRegister.getTooltip());
        this.confirmPasswordTextField.setTooltip(this.confirmPasswordTextFieldVisibleRegister.getTooltip());
    }

    public void handleShowLogin(ActionEvent actionEvent) {
        disableRegister();
        enableLogin();
    }

    private void resetTextFieldColor() {
        this.usernameTextFieldRegister.setStyle("-fx-border-color: transparent transparent #a3a3a3 transparent; -fx-border-width: 0 0 3 0;");
        this.emailTextFieldRegister.setStyle("-fx-border-color: transparent transparent #a3a3a3 transparent; -fx-border-width: 0 0 3 0;");
        this.passwordTextFieldRegister.setStyle("-fx-border-color: transparent transparent #a3a3a3 transparent; -fx-border-width: 0 0 3 0;");
        this.passwordTextFieldVisibleRegister.setStyle("-fx-border-color: transparent transparent #a3a3a3 transparent; -fx-border-width: 0 0 3 0;");
        this.confirmPasswordTextField.setStyle("-fx-border-color: transparent transparent #a3a3a3 transparent; -fx-border-width: 0 0 3 0;");
        this.confirmPasswordTextFieldVisibleRegister.setStyle("-fx-border-color: transparent transparent #a3a3a3 transparent; -fx-border-width: 0 0 3 0;");
    }

    public void handleCreateAccount(ActionEvent actionEvent) {
        resetTextFieldColor();
        String username = this.usernameTextFieldRegister.getText();
        String email = this.emailTextFieldRegister.getText();
        String password, confirmPassword;
        if (this.passwordTextFieldRegister.isVisible()) {
            password = this.passwordTextFieldRegister.getText();
            confirmPassword = this.confirmPasswordTextField.getText();
        } else {
            password = this.passwordTextFieldVisibleRegister.getText();
            confirmPassword = this.confirmPasswordTextFieldVisibleRegister.getText();
        }
        try {
            var oUser = this.masterService.createAccount(username, email, password, confirmPassword);
            handleShowLogin(null);
            this.emailTextFieldLogin.setText(email);
            this.passwordTextFieldLogin.setText(password);

        } catch (ValidationException e) {
            var errorMessage = e.getMessage();
            if (errorMessage.contains("Username")) {
                playAnimation(usernameTextFieldRegister);
                usernameTextFieldRegister.setTooltip(new Tooltip("Username can't be empty!"));
            }
            if (errorMessage.contains("email")) {
                playAnimation(emailTextFieldRegister);
                emailTextFieldRegister.setTooltip(new Tooltip("Invalid e-mail!"));
            }
            if (errorMessage.contains("upper case")) {
                if (passwordTextFieldRegister.isVisible()) {
                    playAnimation(passwordTextFieldRegister);
                    passwordTextFieldRegister.setTooltip(new Tooltip("8+ chars, Uppercase, Number, Special Character"));
                    playAnimation(confirmPasswordTextField);
                } else {
                    playAnimation(passwordTextFieldVisibleRegister);
                    passwordTextFieldRegister.setTooltip(new Tooltip("8+ chars, Uppercase, Number, Special Character"));
                    playAnimation(confirmPasswordTextFieldVisibleRegister);
                }
            }
            if(errorMessage.contains("match")){
                if(passwordTextFieldRegister.isVisible()){
                    confirmPasswordTextField.setTooltip(new Tooltip("Passwords do not match!"));
                } else {
                    confirmPasswordTextFieldVisibleRegister.setTooltip(new Tooltip("Passwords do not match!"));
                }
            }
        }
    }

    private void playAnimation(TextField textField) {
        if(passwordTextFieldRegister.isVisible()) {
            openEyeImagePassRegister.setDisable(true);
            openEyeImageConfRegister.setDisable(true);
        } else {
            closedEyeImagePassRegister.setDisable(true);
            closedEyeImageConfRegister.setDisable(true);
        }

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(textField.styleProperty(),
                        "-fx-border-color: transparent transparent #a3a3a3 transparent; -fx-border-width: 0 0 3 0;")),
                new KeyFrame(Duration.seconds(0.1), new KeyValue(textField.styleProperty(),
                        "-fx-border-color: transparent transparent #E79D9DFF transparent; -fx-border-width: 0 0 3 0;")),
                new KeyFrame(Duration.seconds(0.3), new KeyValue(textField.styleProperty(),
                        "-fx-border-color: transparent transparent #EE7A7AFF transparent ; -fx-border-width: 0 0 3 0;")),
                new KeyFrame(Duration.seconds(0.5), new KeyValue(textField.styleProperty(),
                        "-fx-border-color: transparent transparent #F25151FF transparent; -fx-border-width: 0 0 3 0;")),
                new KeyFrame(Duration.seconds(1), new KeyValue(textField.styleProperty(),
                        "-fx-border-color: transparent transparent #F11A1AFF transparent; -fx-border-width: 0 0 3 0;")),
                new KeyFrame(Duration.seconds(1.2), new KeyValue(textField.styleProperty(),
                        "-fx-border-color: transparent transparent red transparent; -fx-border-width: 0 0 3 0;"))
        );

        timeline.play();
        timeline.setOnFinished(e -> {
            if(passwordTextFieldRegister.isVisible()) {
                openEyeImagePassRegister.setDisable(false);
                openEyeImageConfRegister.setDisable(false);
            } else {
                closedEyeImagePassRegister.setDisable(false);
                closedEyeImageConfRegister.setDisable(false);
            }
        });
    }

    public void handleCloseApp(MouseEvent mouseEvent) {
        WindowManager.getInstance().closeApp();
    }
}
