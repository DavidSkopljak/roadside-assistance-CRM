module com.davidskopljak.skopljakzavrsni {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.logging;
    requires org.slf4j;

    opens com.davidskopljak.skopljakzavrsni.controller to javafx.fxml;
    exports com.davidskopljak.skopljakzavrsni.controller to javafx.graphics;
}