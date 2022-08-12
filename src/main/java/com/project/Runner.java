package com.project;

import com.project.controllers.LobbyController;
import com.project.controllers.TicketController;
import com.project.entity.TicketEntity;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Runner extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        stage.initStyle(StageStyle.UNDECORATED);
        new LobbyController("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjYwMjgxNDY4LCJleHAiOjE2NjA4ODYyNjh9.i8f95mbxo0WrHzB7lRdYkBCPdjEATO_WGL-iVdeGQZAQ7BlRApJvqwtA0STrBslRzkC6hNZOoWHLzS1Q0mHVOA").start(stage);
    }

    public static void main(String[] args){
        launch();
    }

}
