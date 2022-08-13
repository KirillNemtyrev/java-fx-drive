package com.project.controllers;

import com.project.API;
import com.project.Runner;
import com.project.entity.StatisticEntity;
import com.project.entity.TicketEntity;
import com.project.entity.TicketHistoryEntity;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class LobbyController extends Application {

    /* Variables for FXML top panel */
    @FXML
    private ImageView imageCollapse;

    @FXML
    private ImageView imageClose;
    /* Variables for FXML Statistic */
    @FXML
    private PieChart pieChartStatistic;

    @FXML
    private Circle circlePhoto;

    @FXML
    private Label labelName;

    @FXML
    private Label labelStatisticResolved;

    @FXML
    private Label labelStatisticTotal;

    @FXML
    private Label labelStatisticUnDelivery;

    @FXML
    private Label labelStatisticUnResolved;

    @FXML
    private Label labelStatisticProbality;

    @FXML
    private ScrollPane scrollPaneHistory;

    @FXML
    private AnchorPane anchorPaneHistory;
    
    /*Variabels for FXML start ticket */
    
    @FXML
    private Pane paneStartTicket;

    private double offsetPosX;
    private double offsetPosY;

    private static API api = new API();

    public LobbyController(){}
    public LobbyController(String token){
        api.setToken(token);

    }

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Runner.class.getResource("scene/lobby.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 920);
        stage.setResizable(false);
        stage.setTitle("Alpha Test");
        stage.setScene(scene);
        stage.show();

        // For Used to move the scene
        scene.setOnMousePressed(event -> {
            offsetPosX = stage.getX() - event.getScreenX();
            offsetPosY = stage.getY() - event.getScreenY();
        });
        scene.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() + offsetPosX);
            stage.setY(event.getScreenY() + offsetPosY);
        });
    }

    /**
     * This function is launched after opening the scene.
     */

    @FXML
    public void initialize(){

        new ActionPanel();
        new Statistic();
        new Ticket();

    }

    /**
     * This class is responsible for actions with the top panel.
     */
    public class ActionPanel {

        public ActionPanel(){

            eventOnMouseEntered();
            eventOnMouseExit();

            collapseOnMouseClicked();
            closeOnMouseClicked();

        }

        public void eventOnMouseEntered(){

            imageCollapse.setOnMouseEntered(event -> imageCollapse.setOpacity(1.0));
            imageClose.setOnMouseEntered(event -> imageClose.setOpacity(1.0));

        }

        public void eventOnMouseExit(){

            imageCollapse.setOnMouseExited(event -> imageCollapse.setOpacity(0.5));
            imageClose.setOnMouseExited(event -> imageClose.setOpacity(0.5));

        }

        public void collapseOnMouseClicked(){

            imageCollapse.setOnMouseClicked(event -> {
                Stage stage = (Stage) imageCollapse.getScene().getWindow();
                stage.setIconified(true);
            });

        }

        public void closeOnMouseClicked(){

            imageClose.setOnMouseClicked(event -> {
                Stage stage = (Stage) imageClose.getScene().getWindow();
                stage.close();
            });

        }

    }

    /**
     * This class is used to draw statistics.
     */

    public class Statistic {

        public Statistic(){

            anchorPaneHistory.getChildren().clear();

            StatisticEntity statisticEntity = api.getStatistic();

            labelStatisticTotal.setText(String.valueOf(statisticEntity.getTicket().getTotal()));
            labelStatisticResolved.setText(String.valueOf(statisticEntity.getTicket().getResolved()));
            labelStatisticUnResolved.setText(String.valueOf(statisticEntity.getTicket().getUnresolved()));
            labelStatisticUnDelivery.setText(String.valueOf(statisticEntity.getTicket().getUndelivered()));
            labelStatisticProbality.setText(String.format("%(.2f%%", statisticEntity.getTicket().getProbability()));

            PieChart.Data pieChartPass = new PieChart.Data("Сдал", statisticEntity.getTicket().getResolved());
            PieChart.Data pieChartNotPass = new PieChart.Data("Не сдал", statisticEntity.getTicket().getUnresolved());
            PieChart.Data pieChartNotEnd = new PieChart.Data("Не отправлен", statisticEntity.getTicket().getUndelivered());

            pieChartStatistic.getData().addAll(pieChartPass, pieChartNotPass, pieChartNotEnd);

            pieChartPass.getNode().setStyle("-fx-font: 18 Consolas; -fx-pie-color: #078029; -fx-fill: #8f8f8f");
            pieChartNotPass.getNode().setStyle("-fx-font: 18 Consolas; -fx-pie-color: #ae1b1b; -fx-fill: #8f8f8f");
            pieChartNotEnd.getNode().setStyle("-fx-font: 18 Consolas; -fx-pie-color: #dab55e; -fx-fill: #8f8f8f");

            circlePhoto.setFill(new ImagePattern(new Image(statisticEntity.getPhoto())));
            labelName.setText(statisticEntity.getName());

            List<TicketHistoryEntity> ticketHistoryEntities = api.getHistory();
            if(ticketHistoryEntities == null || ticketHistoryEntities.size() == 0){

                ImageView imageView = new ImageView();
                imageView.setLayoutX(142);
                imageView.setLayoutY(184);
                imageView.setFitWidth(265);
                imageView.setFitHeight(285);
                imageView.setImage(new Image(Runner.class.getResource("images/history/handy-meditating-cloud.gif").toString()));

                Label title = new Label("История билетов пока отсутствует...");
                title.setLayoutX(101);
                title.setLayoutY(427);
                title.setPrefWidth(347);
                title.setPrefHeight(22);
                title.setAlignment(Pos.CENTER);
                title.setFont(Font.font("Times New Roman", FontWeight.BOLD, 18));
                title.setTextFill(Paint.valueOf("#9e9e9e"));

                Label description = new Label("После решения билета, он появится тут.");
                description.setLayoutX(107);
                description.setLayoutY(449);
                description.setPrefWidth(335);
                description.setPrefHeight(18);
                description.setAlignment(Pos.CENTER);
                description.setFont(Font.font("Times New Roman", FontWeight.BOLD, 16));
                description.setTextFill(Paint.valueOf("#797777"));

                anchorPaneHistory.getChildren().addAll(imageView, title, description);
                anchorPaneHistory.setPrefHeight(808);
                return;
            }

            Collections.sort(ticketHistoryEntities, new Comparator<TicketHistoryEntity>() {
                @Override
                public int compare(TicketHistoryEntity h, TicketHistoryEntity v) {
                    return v.getTicketDateStart().compareTo(h.getTicketDateStart());
                }
            });

            for(int i = 0; i < ticketHistoryEntities.size(); i ++){

                TicketHistoryEntity ticketHistoryEntity = ticketHistoryEntities.get(i);

                Pane pane = new Pane();
                pane.setLayoutX(0);
                pane.setLayoutY(39 * i);
                pane.setPrefWidth(563);
                pane.setPrefHeight(39);

                String time = new SimpleDateFormat("dd MMMM yyyy", new Locale("ru", "RU")).format(ticketHistoryEntity.getTicketDateStart());
                String clock = new SimpleDateFormat("HH:mm", new Locale("ru", "RU")).format(ticketHistoryEntity.getTicketDateStart());

                Label date = new Label(time + "г. в " + clock);
                date.setLayoutX(14);
                date.setLayoutY(11);
                date.setPrefWidth(179);
                date.setPrefHeight(17);
                date.setFont(Font.font("Times New Roman", FontWeight.BOLD, 13));
                date.setAlignment(Pos.CENTER_LEFT);
                date.setTextFill(Paint.valueOf("#8d8d8d"));

                Label attempts = new Label(String.valueOf(ticketHistoryEntity.getAttempts()));
                attempts.setLayoutX(220);
                attempts.setLayoutY(11);
                attempts.setPrefWidth(50);
                attempts.setPrefHeight(17);
                attempts.setFont(Font.font("Times New Roman", FontWeight.BOLD, 13));
                attempts.setAlignment(Pos.CENTER);
                attempts.setTextFill(Paint.valueOf("#8d8d8d"));

                Label currents = new Label(ticketHistoryEntity.getCorrect() + "/40");
                currents.setLayoutX(329);
                currents.setLayoutY(11);
                currents.setPrefWidth(50);
                currents.setPrefHeight(17);
                currents.setFont(Font.font("Times New Roman", FontWeight.BOLD, 13));
                currents.setAlignment(Pos.CENTER);
                currents.setTextFill(Paint.valueOf("#8d8d8d"));

                Label result = new Label(ticketHistoryEntity.getTicketResultStatus().equals("TICKET_NOT_END") ? "Не отправлен" :
                        ticketHistoryEntity.getTicketResultStatus().equals("TICKET_PASSED") ? "Сдал" : "Не сдал");
                result.setLayoutX(457);
                result.setLayoutY(11);
                result.setPrefWidth(86);
                result.setPrefHeight(17);
                result.setFont(Font.font("Times New Roman", FontWeight.BOLD, 13));
                result.setAlignment(Pos.CENTER);
                result.setTextFill(Paint.valueOf(ticketHistoryEntity.getTicketResultStatus().equals("TICKET_NOT_END") ? "#dab55e" :
                        ticketHistoryEntity.getTicketResultStatus().equals("TICKET_PASSED") ? "#078029" : "#ae1b1b"));

                pane.getChildren().addAll(date, attempts, currents, result);

                anchorPaneHistory.getChildren().add(pane);
            }
            anchorPaneHistory.setPrefHeight(Math.max(39* ticketHistoryEntities.size(), 808));

        }

    }
    
    public class Ticket{
        
        public Ticket(){

            paneStartTicket.setOnMouseEntered(event -> {
                paneStartTicket.setLayoutY(paneStartTicket.getLayoutY() - 1);
                paneStartTicket.setStyle("-fx-background-color: #080808; -fx-background-radius: 25px");
            });

            paneStartTicket.setOnMouseExited(event -> {
                paneStartTicket.setLayoutY(paneStartTicket.getLayoutY() + 1);
                paneStartTicket.setStyle("-fx-background-color: #101010; -fx-background-radius: 25px");
            });

            paneStartTicket.setOnMouseClicked(event -> {

                Stage stage = (Stage) paneStartTicket.getScene().getWindow();
                stage.close();
                try {
                    new TicketController(api.getToken()).start(stage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });
            
        }
        
    }

}
