package com.project;

import com.project.controllers.LobbyController;
import com.project.controllers.TicketController;
import com.project.entity.TicketEntity;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Runner extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        //TicketController ticketController = new TicketController("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjU5NjIzMTc2LCJleHAiOjE2NjAyMjc5NzZ9.vJX-VRYSvPlnKHxOZWyxc6y4kk4MI9f6vbZbda2K9CdbMt-hHMGxMEVyb1mcpqAAO8wX49OdHU_TavEgeVTRmQ");
        //ticketController.start(stage);

        new LobbyController("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjU5NjIzMTc2LCJleHAiOjE2NjAyMjc5NzZ9.vJX-VRYSvPlnKHxOZWyxc6y4kk4MI9f6vbZbda2K9CdbMt-hHMGxMEVyb1mcpqAAO8wX49OdHU_TavEgeVTRmQ").start(stage);
    }

    public static void main(String[] args){
        launch();
    }

}
