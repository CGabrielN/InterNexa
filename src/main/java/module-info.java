module socialnetwork {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens socialnetwork.gui to javafx.fxml;
    exports socialnetwork.gui;

    exports  socialnetwork.service;
    exports  socialnetwork.domain;
    exports  socialnetwork.domain.validators;
    exports  socialnetwork.repository;


}