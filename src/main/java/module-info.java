module com.davidskopljak.skopljakzavrsni {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires com.zaxxer.hikari;
    requires java.sql;
    requires org.slf4j;
    requires ch.qos.logback.classic;
    requires ch.qos.logback.core;

    opens com.davidskopljak.skopljakzavrsni.controller to javafx.fxml;
    exports com.davidskopljak.skopljakzavrsni.controller to javafx.graphics;
}