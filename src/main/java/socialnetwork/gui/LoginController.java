package socialnetwork.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import socialnetwork.service.MasterService;
import socialnetwork.utils.statics.Statics;

public class LoginController implements Window {

    public AnchorPane mainBackground;
    public AnchorPane smallerBackground;
    public AnchorPane widgetsBackground;
    public Hyperlink forgotPasswordLink;
    public Hyperlink signUpLink;
    public TextField passwordTextFieldVisible;
    public ImageView openEyeImage;
    public ImageView closedEyeImage;
    private MasterService masterService;

    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private Label failedLoginLabel;

    private Stage stage;

    private boolean isPasswordVisible = false;

    @Override
    public void setData(MasterService service, Stage stage) {
        this.masterService = service;
        this.stage = stage;

        this.emailTextField.setText("gabriel.cretu@gmail.com");
        this.passwordTextField.setText("gabrielcretu");
//        this.masterService.addObserver(this);
    }

    @FXML
    private void handleLogin(ActionEvent actionEvent){
        String email = emailTextField.getText();
        String password = passwordTextField.getText();
        try{
            var oUser = this.masterService.tryLogin(email, password);
            boolean loginSuccessful = oUser.isPresent();
            if(loginSuccessful){
                this.hide();

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/socialnetwork/gui/user-view.fxml"));

                AnchorPane root = loader.load();

                Stage dialogStage = new Stage();
                dialogStage.setTitle(oUser.get().getUsername());
                dialogStage.initModality(Modality.WINDOW_MODAL);
                Scene scene = new Scene(root);
                dialogStage.setScene(scene);
//
//                UserController userController = loader.getController();
//                userController.setMasterService(this.masterService, dialogStage, oUser.get());

                dialogStage.show();


            }else{
                failedLoginLabel.setVisible(true);
            }
        } catch (Exception e){
            MessageAlert.showErrorMessage(null, e.getMessage());
        }


    }

    @Override
    public void show(){
        this.stage.setWidth(Statics.loginWindowWidth);
        this.stage.setHeight(Statics.loginWindowHeight);
//        this.stage.setMaximized(true);
        this.stage.setY(0f);
        this.stage.setX(0f);

//        System.out.println(this.stage.getWidth());
//        System.out.println(this.stage.getHeight());

        this.passwordTextField.setVisible(true);
        this.openEyeImage.setVisible(true);
        this.passwordTextFieldVisible.setVisible(false);
        this.closedEyeImage.setVisible(false);
        this.isPasswordVisible = false;

//        this.stage.centerOnScreen();
        if(this.failedLoginLabel.isVisible())
            this.failedLoginLabel.setVisible(false);
    }

    @Override
    public void hide(){
        if(this.failedLoginLabel.isVisible())
            this.failedLoginLabel.setVisible(false);
        this.emailTextField.clear();
        this.passwordTextField.clear();
        this.passwordTextFieldVisible.clear();
    }


//    @Override
//    public void update(Event event) {
//        AbstractEvent abstractEvent = (AbstractEvent) event;
//        if(abstractEvent.getEventType() == ChangeEventType.LOGIN)
//            show();
//    }


    @FXML
    private void handleRegister(ActionEvent actionEvent){
        WindowManager.getInstance().hideLoginWindow();
        WindowManager.getInstance().showRegisterWindow();
    }

    public void handleShowPassword(MouseEvent mouseEvent) {
        String password = passwordTextField.getText();
        this.passwordTextField.setVisible(false);
        this.openEyeImage.setVisible(false);

        this.passwordTextFieldVisible.setText(password);
        this.passwordTextFieldVisible.setVisible(true);
        this.closedEyeImage.setVisible(true);

        this.isPasswordVisible = true;
    }

    public void handleHidePassword(MouseEvent mouseEvent) {
        String password = passwordTextFieldVisible.getText();
        this.passwordTextFieldVisible.setVisible(false);
        this.closedEyeImage.setVisible(false);

        this.passwordTextField.setText(password);
        this.passwordTextField.setVisible(true);
        this.openEyeImage.setVisible(true);

        this.isPasswordVisible = false;
    }
}
