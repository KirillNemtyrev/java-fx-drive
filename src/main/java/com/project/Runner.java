package com.project;

import com.project.controllers.AuthController;
import com.project.controllers.LobbyController;
import com.project.controllers.TicketController;
import com.project.entity.TicketEntity;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class Runner extends Application {

    @Override
    public void start(Stage stage) throws IOException, URISyntaxException {

        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        stage.setTitle("ПДД РК");
        stage.getIcons().add(new Image(Runner.class.getResource("images/rules/rules.png").toURI().toString()));

        Config config = new Config();
        if(config.token != null && new API().checkToken(config.token)){
            new LobbyController(config.token).start(stage);
            return;
        }
        new AuthController().start(stage);
    }

    public static void main(String[] args){
        launch();
    }

}
