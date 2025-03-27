module com.example.facultativ_javafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.sql.rowset;

    opens com.example.facultativ_javafx to javafx.fxml;
    exports com.example.facultativ_javafx;
}