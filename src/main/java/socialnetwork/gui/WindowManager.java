package socialnetwork.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.User;
import socialnetwork.service.MasterService;

import java.io.IOException;
import java.util.Optional;

public class WindowManager {

    private static final WindowManager instance = new WindowManager();

    private WindowManager() {
    }

    public static WindowManager getInstance() {
        return instance;
    }

    private Stage stage;
    private MasterService service;

    private User currentUser;

    private Tuple<LoginController, Scene> login;

    private Tuple<RegisterController, Scene> register;


    public void setData(Stage stage, MasterService service) {
        this.stage = stage;
        this.service = service;
        this.currentUser = null;
    }

    public void showLoginWindow(){
        if(this.login == null)
            this.login = new Tuple<>(null, null);

        var res = this.showWindow("/socialnetwork/views/login-view.fxml", this.login.getLeft(), this.login.getRight());
        this.login = new Tuple<>((LoginController) res.getLeft(), res.getRight());
    }

    public void hideLoginWindow() {
        if(this.login.getLeft() != null){
            this.login.getLeft().hide();
        }
    }

    public void showRegisterWindow(User... loggedUser){
        if(this.currentUser == null)
            this.currentUser = loggedUser[0];

        if(this.register == null)
            this.register = new Tuple<>(null, null);

        var res = this.showWindow("/socialnetwork/views/register-view.fxml", this.register.getLeft(),this.register.getRight());
        this.register = new Tuple<>((RegisterController) res.getLeft(), res.getRight());
    }


    public void hideRegisterWindow(){
        if(this.register.getLeft() != null){
            this.register.getLeft().hide();
        }
    }

    public void closeApp(){
        this.stage.close();
    }

    private Tuple<Window, Scene> showWindow(String fxmlPath, Window window, Scene scene){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(fxmlPath));

            if(scene == null){
                AnchorPane root = loader.load();
                scene = new Scene(root);
            }

            stage.setScene(scene);

            if(window == null) {
                window = loader.getController();
                window.setData(service, stage);
            }
            window.show();

        } catch (IOException e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
        return new Tuple<>(window, scene);
    }

    public void showSplashScreen(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/socialnetwork/views/splash-screen.fxml"));
            AnchorPane root = loader.load();
            Scene scene = new Scene(root);

            stage.setScene(scene);
            SplashScreenController window = loader.getController();
            window.setData(service, stage);
            window.show();

        } catch (IOException e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }
}
