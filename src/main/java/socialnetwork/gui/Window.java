package socialnetwork.gui;

import javafx.stage.Stage;
import socialnetwork.service.MasterService;

public interface Window {

    void setData(MasterService service, Stage stage);

    void show();

    void hide();
}
