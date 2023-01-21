module com.example.atm_online {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.atm_online to javafx.fxml;
    exports com.example.atm_online;
}