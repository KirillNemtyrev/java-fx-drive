package com.project;

import com.project.controllers.TicketController;
import com.project.entity.TicketEntity;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Runner extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        API api = new API();
        TicketEntity ticketEntity = api.startTicket();

        TicketController ticketController = new TicketController(ticketEntity);
        ticketController.setTicketEntity(ticketEntity);
        ticketController.start(stage);
    }

    public static void main(String[] args){
        launch();
    }

}
