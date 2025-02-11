module com.davidskopljak.skopljakzavrsni {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.davidskopljak.skopljakzavrsni to javafx.fxml;
    exports com.davidskopljak.skopljakzavrsni;
    opens com.davidskopljak.skopljakzavrsni.controller to javafx.fxml;
    exports com.davidskopljak.skopljakzavrsni.controller to javafx.graphics;
}