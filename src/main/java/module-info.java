module com.application.javafxdrive {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;

    requires com.google.gson;

    opens com.project.entity to com.google.gson;

    opens com.project to javafx.fxml;
    exports com.project;

    opens com.project.controllers to javafx.fxml;
    exports com.project.controllers;
}