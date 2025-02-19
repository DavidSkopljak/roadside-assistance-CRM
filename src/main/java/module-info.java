module com.davidskopljak.skopljakzavrsni {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.zaxxer.hikari;
    requires org.slf4j;
    requires ch.qos.logback.classic;
    requires ch.qos.logback.core;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.databind;
    requires java.sql;

    opens com.davidskopljak.skopljakzavrsni.controller to javafx.fxml;
    exports com.davidskopljak.skopljakzavrsni.controller to javafx.graphics;
    exports com.davidskopljak.skopljakzavrsni.entity to com.fasterxml.jackson.databind;
}