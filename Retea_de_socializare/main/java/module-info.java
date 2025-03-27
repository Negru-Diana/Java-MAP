module com.example.retea_de_socializare {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires java.desktop;
    requires jdk.jfr;

    opens com.example.retea_de_socializare to javafx.fxml;
    opens com.example.retea_de_socializare.controllers to javafx.fxml;


    exports com.example.retea_de_socializare;
    exports com.example.retea_de_socializare.domain;
    exports com.example.retea_de_socializare.controllers;
    opens com.example.retea_de_socializare.controllers.utilsControllers to javafx.fxml;
    opens com.example.retea_de_socializare.controllers.customListView to javafx.fxml;
    exports com.example.retea_de_socializare.controllers.customListView.customMembriGrupListView;
    opens com.example.retea_de_socializare.controllers.customListView.customMembriGrupListView to javafx.fxml;
}