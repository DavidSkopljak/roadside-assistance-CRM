module com.davidskopljak.skopljakzavrsni {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.zaxxer.hikari;
    requires org.slf4j;
    requires ch.qos.logback.classic;
    requires ch.qos.logback.core;
    requires java.sql;
    requires jdk.jconsole;
    requires com.sothawo.mapjfx;
    requires json.simple;
    requires java.net.http;

    opens com.davidskopljak.skopljakzavrsni.controller to javafx.fxml;
    exports com.davidskopljak.skopljakzavrsni.controller to javafx.graphics;
    exports com.davidskopljak.skopljakzavrsni.entity to com.fasterxml.jackson.databind;
}