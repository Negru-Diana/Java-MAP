module com.example.examen_db {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.examen_db to javafx.fxml;
    exports com.example.examen_db;
}