package socialnetwork.gui;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import socialnetwork.service.MasterService;
import socialnetwork.utils.statics.Statics;

public class SplashScreenController implements Window {

    public Label loadingLabel;
    @FXML
    private AnchorPane rootPane;

    private double fadeInDuration = 1.5;
    private double fadeLoadingDuration = 1;


    private Stage stage;

    @Override
    public void setData(MasterService service, Stage stage) {
        this.stage = stage;

    }

    private void display() {
        loadingLabel.setOpacity(0);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(fadeInDuration), rootPane);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        fadeIn.play();

        fadeIn.setOnFinished(e -> {
            FadeTransition fadeInTransition1 = new FadeTransition(Duration.seconds(fadeLoadingDuration), loadingLabel);
            fadeInTransition1.setFromValue(0);
            fadeInTransition1.setToValue(1);

            fadeInTransition1.play();
            fadeInTransition1.setOnFinished(event1 -> {
                FadeTransition fadeOutTransition1 = new FadeTransition(Duration.seconds(fadeLoadingDuration), loadingLabel);
                fadeOutTransition1.setFromValue(1);
                fadeOutTransition1.setToValue(0);

                fadeOutTransition1.play();
                fadeOutTransition1.setOnFinished(event2 -> {
                    FadeTransition fadeInTransition2 = new FadeTransition(Duration.seconds(fadeLoadingDuration), loadingLabel);
                    fadeInTransition2.setFromValue(0);
                    fadeInTransition2.setToValue(1);

                    fadeInTransition2.play();
                    fadeInTransition2.setOnFinished(event3 -> {
                        FadeTransition fadeOutTransition2 = new FadeTransition(Duration.seconds(fadeLoadingDuration), loadingLabel);
                        fadeOutTransition2.setFromValue(1);
                        fadeOutTransition2.setToValue(0);

                        fadeOutTransition2.play();

                        fadeOutTransition2.setOnFinished(event4 -> {
                            // load the app
                            WindowManager.getInstance().showLoginWindow();
                        });
                    });
                });
            });
        });
    }

    @Override
    public void show() {
        this.stage.setWidth(Statics.splashScreenWindowWidth);
        this.stage.setHeight(Statics.splashScreenWindowHeight);
        display();
    }

    @Override
    public void hide() {

    }
}
