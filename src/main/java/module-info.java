module socialnetwork.internexa {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens socialnetwork to javafx.fxml;
    exports socialnetwork;


}