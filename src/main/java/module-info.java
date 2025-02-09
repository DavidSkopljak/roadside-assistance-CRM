module java.main.controller {
    requires javafx.controls;
    requires javafx.fxml;


    opens java.main.controller to javafx.fxml;
    exports java.main.controller;
}