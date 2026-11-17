module com.example.gestionbib {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.gestionbib to javafx.fxml;
    opens com.example.gestionbib.data to javafx.base;

    exports com.example.gestionbib;
}