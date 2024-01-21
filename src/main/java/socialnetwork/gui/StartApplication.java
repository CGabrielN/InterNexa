package socialnetwork.gui;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import socialnetwork.service.MasterService;

import java.io.IOException;

public class StartApplication extends Application {

    private static MasterService masterService;

    @Override
    public void start(Stage stage) throws IOException {
        WindowManager.getInstance().setData(stage, masterService);
        WindowManager.getInstance().showSplashScreen();

        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    public static void main(String[] args) {
        masterService = new MasterService(args[0], args[1], args[2]);

        launch();
    }

}