package com.project.controllers;

import com.project.API;
import com.project.Runner;
import com.project.draw.TicketNumbers;
import com.project.entity.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TicketController extends Application {

    @FXML
    private AnchorPane anchorPaneQuestions;

    @FXML
    private Button buttonRetryConnect;

    @FXML
    private Group groupCloseMenuTicket;

    @FXML
    private Group groupNoConnect;

    @FXML
    private Group groupTicketResult;

    @FXML
    private ImageView imageClose;

    @FXML
    private ImageView imageCloseMenuEnd;

    @FXML
    private ImageView imageCloseResult;

    @FXML
    private ImageView imageCollapse;

    @FXML
    private Label labelAnswered;

    @FXML
    private Label labelEndTicket;

    @FXML
    private Label labelNumberQuestion;

    @FXML
    private Label labelResultAnswered;

    @FXML
    private Label labelResultCorrect;

    @FXML
    private Label labelResultPass;

    @FXML
    private Label labelRetryConnect;

    @FXML
    private Label labelTextQuestion;

    @FXML
    private Label labelTicketTime;

    @FXML
    private Label labelVersion;

    @FXML
    private Pane paneBackLobby;

    @FXML
    private Pane paneBackgroundClose;

    @FXML
    private Pane paneBackgroundResult;

    @FXML
    private Pane paneCloseTicket;

    @FXML
    private Pane paneEndTicket;

    @FXML
    private Pane paneNewTicket;

    @FXML
    private Pane paneResultBackLobby;

    @FXML
    private Pane paneResultNewTicket;

    @FXML
    private Pane paneResultRetryTicket;

    @FXML
    private Pane paneResultTicket;

    @FXML
    private Pane paneRetryTicket;

    @FXML
    private Pane paneTicket;

    @FXML
    private Pane paneTicketTime;

    @FXML
    private ScrollPane scrollPaneQuestions;

    private double offsetPosX;
    private double offsetPosY;

    private static TicketEntity ticketEntity;

    public static Timer timerRetry;
    private static long countdownRetry;

    private static API api = new API();

    public TicketController(){}

    public TicketController(String token){
        api.setToken(token);

        ticketEntity = api.startTicket();
    }

    public TicketController(String token, String uuid){
        api.setToken(token);

        ticketEntity = api.retryTicket(uuid);
    }

    public void setTicketEntity(TicketEntity ticketEntity){
        this.ticketEntity = ticketEntity;
    }

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Runner.class.getResource("scene/ticket.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 920);
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
        new TicketQuestions(40);
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
     * This class is used to draw the ticket.
     */

    public class TicketQuestions {

        public static List<TicketNumbers> ticketNumbers = new ArrayList<>();
        public static Long[] answers = new Long[40];
        public static int currentQuestion = 0;

        public static Timer timer;
        private Label labelChoise;

        public TicketQuestions(){
        }

        /**
         * The function is used to draw ticket numbers.
         * @param count
         */
        public TicketQuestions(int count){

            if(timer != null){
                timer.cancel();
                timer = null;
            }
            paneTicketTime.setVisible(true);
            labelTicketTime.setText("--:--");
            timer = new Timer();
            timer.schedule(new TaskTimer(ticketEntity.getDateStart(), ticketEntity.getDateEnd()), 1000, 1000);

            ticketNumbers.clear();
            paneEndTicket.setVisible(true);
            groupTicketResult.setVisible(false);
            labelEndTicket.setText("Завершить");

            paneEndTicket.setOnMouseEntered(event -> paneEndTicket.setStyle("-fx-background-color: #101010"));
            paneEndTicket.setOnMouseExited(event -> paneEndTicket.setStyle("-fx-background-color: #141414"));
            paneEndTicket.setOnMouseClicked(event -> {

                if(!API.pingHost()){
                    new NoConnect();
                    return;
                }

                groupCloseMenuTicket.setVisible(true);
            });
            paneBackgroundClose.setOnMouseClicked(event -> groupCloseMenuTicket.setVisible(false));

            imageCloseMenuEnd.setOnMouseEntered(event -> imageCloseMenuEnd.setOpacity(1.0));
            imageCloseMenuEnd.setOnMouseExited(event -> imageCloseMenuEnd.setOpacity(0.5));
            imageCloseMenuEnd.setOnMouseClicked(event -> {
                if(!API.pingHost()){
                    new NoConnect();
                    return;
                }

                groupCloseMenuTicket.setVisible(false);
            });

            paneCloseTicket.setOnMouseEntered(event -> {
                paneCloseTicket.setLayoutY(paneCloseTicket.getLayoutY() - 1);
                paneCloseTicket.setStyle("-fx-background-color: #080808");
            });

            paneCloseTicket.setOnMouseExited(event -> {
                paneCloseTicket.setLayoutY(paneCloseTicket.getLayoutY() + 1);
                paneCloseTicket.setStyle("-fx-background-color: #111111");
            });

            paneCloseTicket.setOnMouseClicked(event -> {

                if(!API.pingHost()){
                    new NoConnect();
                    return;
                }

                new CloseTicket();
                groupCloseMenuTicket.setVisible(false);
                groupTicketResult.setVisible(true);
            });

            paneRetryTicket.setOnMouseEntered(event -> {
                paneRetryTicket.setLayoutY(paneRetryTicket.getLayoutY() - 1);
                paneRetryTicket.setStyle("-fx-background-color: #080808");
            });

            paneRetryTicket.setOnMouseExited(event -> {
                paneRetryTicket.setLayoutY(paneRetryTicket.getLayoutY() + 1);
                paneRetryTicket.setStyle("-fx-background-color: #111111");
            });

            paneRetryTicket.setOnMouseClicked(event -> {

                if(!API.pingHost()){
                    new NoConnect();
                    return;
                }

                groupCloseMenuTicket.setVisible(false);
                groupTicketResult.setVisible(false);

                ticketEntity = api.retryTicket(ticketEntity.getUuid());

                new TicketQuestions(40);

            });

            paneNewTicket.setOnMouseEntered(event -> {
                paneNewTicket.setLayoutY(paneNewTicket.getLayoutY() - 1);
                paneNewTicket.setStyle("-fx-background-color: #080808");
            });

            paneNewTicket.setOnMouseExited(event -> {
                paneNewTicket.setLayoutY(paneNewTicket.getLayoutY() + 1);
                paneNewTicket.setStyle("-fx-background-color: #111111");
            });

            paneNewTicket.setOnMouseClicked(event -> {

                if(!API.pingHost()){
                    new NoConnect();
                    return;
                }

                groupCloseMenuTicket.setVisible(false);
                groupTicketResult.setVisible(false);

                api.endTicket(ticketEntity.getUuid(), TicketQuestions.answers);
                ticketEntity = api.startTicket();

                new TicketQuestions(40);
            });

            paneBackLobby.setOnMouseEntered(event -> {
                paneBackLobby.setLayoutY(paneBackLobby.getLayoutY() - 1);
                paneBackLobby.setStyle("-fx-background-color: #390202");
            });
            paneBackLobby.setOnMouseExited(event -> {
                paneBackLobby.setLayoutY(paneBackLobby.getLayoutY() + 1);
                paneBackLobby.setStyle("-fx-background-color: #400000");
            });
            paneBackLobby.setOnMouseClicked(event -> {

                if(!API.pingHost()){
                    new NoConnect();
                    return;
                }

                Stage stage = (Stage) paneBackLobby.getScene().getWindow();
                stage.close();

                try {
                    new LobbyController(api.getToken()).start(stage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });

            for(int temp = 0; temp < count; temp++){

                labelAnswered.setText("0/40");

                answers[temp] = null;

                Pane pane = new Pane();
                pane.setLayoutX(0);
                pane.setLayoutY(54*temp);
                pane.setPrefWidth(69);
                pane.setPrefHeight(54);
                pane.setStyle("-fx-background-color: " + (temp == 0 ? "#080808" : "#141414"));

                int number = temp;
                pane.setOnMouseEntered(event -> pane.setStyle("-fx-background-color: " + (currentQuestion == number ? "#050505" : "#111111")));
                pane.setOnMouseExited(event -> pane.setStyle("-fx-background-color: " + (currentQuestion == number ? "#080808" : "#141414")));
                pane.setOnMouseClicked(event -> {
                    if(!API.pingHost()){
                        new NoConnect();
                        return;
                    }

                    if(currentQuestion == number){
                        return;
                    }

                    ticketNumbers.get(currentQuestion).getPane().setStyle("-fx-background-color: #141414");
                    pane.setStyle("-fx-background-color: #080808");
                    selectQuestion(number);
                });
                anchorPaneQuestions.getChildren().add(pane);

                Label label = new Label(String.valueOf(temp + 1));
                label.setLayoutX(15);
                label.setLayoutY(15);
                label.setPrefWidth(40);
                label.setPrefHeight(17);
                label.setAlignment(Pos.CENTER);
                label.setFont(Font.font("Times New Roman", FontWeight.BOLD, 18));
                label.setTextFill(Paint.valueOf("#c6c6c6"));
                pane.getChildren().add(label);

                ticketNumbers.add(new TicketNumbers(pane, label));
            }

            anchorPaneQuestions.setPrefHeight(Math.max(54 * count, 866));
            selectQuestion(0);
        }

        /**
         * The function is used to draw the ticket itself.
         * @param question
         */
        @FXML
        public void selectQuestion(int question){

            if(!API.pingHost()){
                new NoConnect();
                return;
            }

            ticketNumbers.get(currentQuestion).getPane().setStyle("-fx-background-color: #141414");
            ticketNumbers.get(question).getPane().setStyle("-fx-background-color: #080808");

            //scrollPaneQuestions.setVvalue(1 / (float) ticketNumbers.get(question).getPane().getLayoutY());

            currentQuestion = question;
            labelChoise = null;

            TicketQuestionsEntity ticketQuestions = ticketEntity.getTicket(question);

            labelNumberQuestion.setText("Вопрос #" + (question + 1));
            labelTextQuestion.setText(ticketQuestions.getText());

            paneTicket.getChildren().clear();

            int value = 5;
            for(TicketAnswersEntity ticketAnswersEntity : ticketQuestions.getAnswers()){

                Text text = new Text(ticketAnswersEntity.getText());
                text.setWrappingWidth(ticketQuestions.getPhoto() != null ? 416 : 765);
                text.setFont(Font.font("Times New Roman", FontWeight.BOLD, 16));

                Pane pane = new Pane();
                pane.setLayoutX(ticketQuestions.getPhoto() != null ? 654 : 0);
                pane.setLayoutY(value);
                pane.setPrefWidth(ticketQuestions.getPhoto() != null ? 462 : 811);
                pane.setPrefHeight(text.getLayoutBounds().getHeight() + 26);
                pane.setStyle("-fx-background-color: #141414; -fx-background-radius: 25px");

                Label label = new Label(ticketAnswersEntity.getText());
                label.setWrapText(true);
                label.setLayoutX(25);
                label.setLayoutY(6);
                label.setPrefWidth(text.getLayoutBounds().getWidth());
                label.setPrefHeight(text.getLayoutBounds().getHeight() + 15);
                label.setTextFill(Paint.valueOf("#909090"));
                label.setFont(Font.font("Times New Roman", FontWeight.BOLD, 16));
                pane.getChildren().add(label);

                if(answers[question] != null && answers[question] == ticketAnswersEntity.getNumber()){
                    label.setTextFill(Paint.valueOf("#754e0a"));
                    labelChoise = label;
                }

                value += pane.getPrefHeight() + 10;
                paneTicket.getChildren().add(pane);

                pane.setOnMouseEntered(event -> {
                    pane.setLayoutY(pane.getLayoutY() - 1);
                    pane.setStyle("-fx-background-color: #121212; -fx-background-radius: 25px");
                });

                pane.setOnMouseExited(event -> {
                    pane.setLayoutY(pane.getLayoutY() + 1);
                    pane.setStyle("-fx-background-color: #141414; -fx-background-radius: 25px");
                });

                pane.setOnMouseClicked(event -> {

                    if(answers[question] != null && answers[question] == ticketAnswersEntity.getNumber()){
                        return;
                    }

                    answers[question] = ticketAnswersEntity.getNumber();
                    label.setTextFill(Paint.valueOf("#754e0a"));

                    if(labelChoise != null) {
                        labelChoise.setTextFill(Paint.valueOf("#909090"));
                    }

                    labelChoise = label;

                    TicketNumbers tickets = ticketNumbers.get(question);
                    tickets.getLabel().setTextFill(Paint.valueOf("#754e0a"));

                    labelAnswered.setText(getAnswered() + "/40");
                });
            }

            Pane paneBack = new Pane();
            paneBack.setPrefWidth(132);
            paneBack.setPrefHeight(44);
            paneBack.setStyle("-fx-background-color: #141414");
            paneBack.setVisible(question != 0);

            Label labelBack = new Label("« Назад");
            labelBack.setLayoutX(26);
            labelBack.setLayoutY(11);
            labelBack.setPrefWidth(76);
            labelBack.setPrefHeight(23);
            labelBack.setFont(Font.font("Monospaced", FontWeight.BOLD, 18));
            labelBack.setTextFill(Paint.valueOf("#9e9e9e"));
            paneBack.getChildren().add(labelBack);

            Pane paneNext = new Pane();
            paneNext.setPrefWidth(132);
            paneNext.setPrefHeight(44);
            paneNext.setVisible(question != 39);
            paneNext.setStyle("-fx-background-color: #141414");

            Label labelNext = new Label("Далее »");
            labelNext.setLayoutX(26);
            labelNext.setLayoutY(11);
            labelNext.setPrefWidth(76);
            labelNext.setPrefHeight(23);
            labelNext.setFont(Font.font("Monospaced", FontWeight.BOLD, 18));
            labelNext.setTextFill(Paint.valueOf("#9e9e9e"));
            paneNext.getChildren().add(labelNext);

            if(ticketQuestions.getPhoto() != null) {

                ImageView imageView = new ImageView(new Image(ticketQuestions.getPhoto()));
                imageView.setLayoutX(0);
                imageView.setLayoutY(0);
                imageView.setFitWidth(635);
                imageView.setFitHeight(446);
                paneTicket.getChildren().add(imageView);
            }

            paneBack.setLayoutY(value + 10);
            paneNext.setLayoutY(value + 10);

            paneBack.setLayoutX(ticketQuestions.getPhoto() != null ? 654 : 0);
            paneNext.setLayoutX(paneBack.isVisible() ?
                    (ticketQuestions.getPhoto() != null ? paneBack.getLayoutX() + 325 : paneBack.getLayoutX() + 675) :
                    ticketQuestions.getPhoto() != null ? 654 : 0);
            paneTicket.getChildren().addAll(paneBack, paneNext);

            paneBack.setOnMouseEntered(event -> {
                paneBack.setStyle("-fx-background-color: #121212");
                paneBack.setLayoutY(paneBack.getLayoutY() - 1);
            });
            paneBack.setOnMouseExited(event -> {
                paneBack.setStyle("-fx-background-color: #141414");
                paneBack.setLayoutY(paneBack.getLayoutY() + 1);
            });
            paneBack.setOnMouseClicked(event -> {
                selectQuestion(question - 1);
            });

            paneNext.setOnMouseEntered(event -> {
                paneNext.setStyle("-fx-background-color: #121212");
                paneNext.setLayoutY(paneNext.getLayoutY() - 1);
            });
            paneNext.setOnMouseExited(event -> {
                paneNext.setStyle("-fx-background-color: #141414");
                paneNext.setLayoutY(paneNext.getLayoutY() + 1);
            });
            paneNext.setOnMouseClicked(event -> {
                selectQuestion(question + 1);
            });
        }

        @FXML
        public int getAnswered(){

            int ammount = 0;
            for(int count = 0; count < answers.length; count++){

                if(answers[count] == null){
                    continue;
                }

                ammount++;
            }
            return ammount;
        }

    }

    public class CloseTicket{

        private TicketEndEntity ticketEndEntity;
        public static int currentQuestion = 0;

        public CloseTicket(){

            paneTicketTime.setVisible(false);
            labelTicketTime.setText("--:--");
            if(TicketQuestions.timer != null){

                TicketQuestions.timer.cancel();
                TicketQuestions.timer = null;

            }

            labelEndTicket.setText("Результат");
            paneEndTicket.setOnMouseClicked(event -> {
                if(!API.pingHost()){
                    new NoConnect();
                    return;
                }

                groupTicketResult.setVisible(true);
            });

            if(!API.pingHost()){
                new NoConnect();
                return;
            }

            this.ticketEndEntity = api.endTicket(ticketEntity.getUuid(), TicketQuestions.answers);
            currentQuestion = TicketQuestions.currentQuestion;

            imageCloseResult.setOnMouseEntered(event -> imageCloseResult.setOpacity(1.0));
            imageCloseResult.setOnMouseExited(event -> imageCloseResult.setOpacity(0.5));
            imageCloseResult.setOnMouseClicked(event -> {
                if(!API.pingHost()){
                    new NoConnect();
                    return;
                }

                groupTicketResult.setVisible(false);
            });
            paneBackgroundResult.setOnMouseClicked(event -> {

                if(!API.pingHost()){
                    new NoConnect();
                    return;
                }

                groupTicketResult.setVisible(false);
            });

            labelResultAnswered.setText("Вы ответили на " + new TicketQuestions().getAnswered() + " вопросов.");
            labelResultCorrect.setText(String.valueOf(ticketEndEntity.getCorrect()));
            labelResultPass.setText(ticketEndEntity.getTicketResultStatus() == 1 ? "Сдал" : "Не сдал");
            labelResultPass.setTextFill(Paint.valueOf(ticketEndEntity.getTicketResultStatus() == 1 ? "#078029" : "#ae1b1b"));

            paneResultTicket.setOnMouseClicked(event -> {

                if(!API.pingHost()){
                    new NoConnect();
                    return;
                }

                groupTicketResult.setVisible(false);
            });
            paneResultTicket.setOnMouseEntered(event -> {
                paneResultTicket.setLayoutY(paneResultTicket.getLayoutY() - 1);
                paneResultTicket.setStyle("-fx-background-color: #080808");
            });

            paneResultTicket.setOnMouseExited(event -> {
                paneResultTicket.setLayoutY(paneResultTicket.getLayoutY() + 1);
                paneResultTicket.setStyle("-fx-background-color: #111111");
            });

            paneResultNewTicket.setOnMouseClicked(event -> {

                if(!API.pingHost()){
                    new NoConnect();
                    return;
                }

                groupCloseMenuTicket.setVisible(false);
                groupTicketResult.setVisible(false);

                ticketEntity = api.startTicket();
                new TicketQuestions(40);
            });
            paneResultNewTicket.setOnMouseEntered(event -> {
                paneResultNewTicket.setLayoutY(paneResultNewTicket.getLayoutY() - 1);
                paneResultNewTicket.setStyle("-fx-background-color: #080808");
            });

            paneResultNewTicket.setOnMouseExited(event -> {
                paneResultNewTicket.setLayoutY(paneResultNewTicket.getLayoutY() + 1);
                paneResultNewTicket.setStyle("-fx-background-color: #111111");
            });

            paneResultRetryTicket.setOnMouseClicked(event -> {

                if(!API.pingHost()){
                    new NoConnect();
                    return;
                }

                groupCloseMenuTicket.setVisible(false);
                groupTicketResult.setVisible(false);

                ticketEntity = api.retryTicket(ticketEntity.getUuid());
                new TicketQuestions(40);
            });
            paneResultRetryTicket.setOnMouseEntered(event -> {
                paneResultRetryTicket.setLayoutY(paneResultRetryTicket.getLayoutY() - 1);
                paneResultRetryTicket.setStyle("-fx-background-color: #080808");
            });

            paneResultRetryTicket.setOnMouseExited(event -> {
                paneResultRetryTicket.setLayoutY(paneResultRetryTicket.getLayoutY() + 1);
                paneResultRetryTicket.setStyle("-fx-background-color: #111111");
            });

            paneResultBackLobby.setOnMouseEntered(event -> {
                paneResultBackLobby.setLayoutY(paneResultBackLobby.getLayoutY() - 1);
                paneResultBackLobby.setStyle("-fx-background-color: #390202");
            });
            paneResultBackLobby.setOnMouseExited(event -> {
                paneResultBackLobby.setLayoutY(paneResultBackLobby.getLayoutY() + 1);
                paneResultBackLobby.setStyle("-fx-background-color: #400000");
            });
            paneResultBackLobby.setOnMouseClicked(event -> {

                if(!API.pingHost()){
                    new NoConnect();
                    return;
                }

                Stage stage = (Stage) paneResultBackLobby.getScene().getWindow();
                stage.close();

                try {
                    new LobbyController(api.getToken()).start(stage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });

            TicketQuestions();

        }

        public void TicketQuestions(){

            for(int temp = 0; temp < TicketQuestions.ticketNumbers.size(); temp++){

                TicketWithAnswersEntity ticketQuestions = ticketEndEntity.getTicket(temp);
                TicketQuestions.ticketNumbers.get(temp).getLabel().setTextFill(
                        Paint.valueOf(
                                ticketQuestions.getCorrect() == ticketQuestions.getAnswer() ? "#078029" :
                                        ticketQuestions.getCorrect() != ticketQuestions.getAnswer() && ticketQuestions.getAnswer() != null ? "#ae1b1b" : "#dab55e"));

                int number = temp;
                TicketQuestions.ticketNumbers.get(number).getPane().setOnMouseEntered(event ->
                        TicketQuestions.ticketNumbers.get(number).getPane().setStyle("-fx-background-color: " + (currentQuestion == number ? "#050505" : "#111111")));
                TicketQuestions.ticketNumbers.get(number).getPane().setOnMouseExited(event ->
                        TicketQuestions.ticketNumbers.get(number).getPane().setStyle("-fx-background-color: " + (currentQuestion == number ? "#080808" : "#141414")));
                TicketQuestions.ticketNumbers.get(temp).getPane().setOnMouseClicked(event -> {

                    if(currentQuestion == number){
                        return;
                    }

                    if(TicketQuestions.ticketNumbers.get(currentQuestion).getPane() != null) {
                        TicketQuestions.ticketNumbers.get(currentQuestion).getPane().setStyle("-fx-background-color: #141414");
                    }
                    TicketQuestions.ticketNumbers.get(number).getPane().setStyle("-fx-background-color: #080808");
                    drawQuestion(number);
                });

            }

            anchorPaneQuestions.setPrefHeight(Math.max(54 * TicketQuestions.ticketNumbers.size(), 866));
            drawQuestion(0);
        }

        public void drawQuestion(int question){

            if(!API.pingHost()){
                new NoConnect();
                return;
            }

            TicketQuestions.ticketNumbers.get(currentQuestion).getPane().setStyle("-fx-background-color: #141414");
            TicketQuestions.ticketNumbers.get(question).getPane().setStyle("-fx-background-color: #080808");

            currentQuestion = question;

            TicketWithAnswersEntity ticketQuestions = ticketEndEntity.getTicket(question);

            labelNumberQuestion.setText("Вопрос #" + (question + 1));
            labelTextQuestion.setText(ticketQuestions.getText());

            paneTicket.getChildren().clear();

            int value = 5;
            for(TicketAnswersEntity ticketAnswersEntity : ticketQuestions.getAnswers()){

                Text text = new Text(ticketAnswersEntity.getText());
                text.setWrappingWidth(ticketQuestions.getPhoto() != null ? 416 : 765);
                text.setFont(Font.font("Times New Roman", FontWeight.BOLD, 16));

                Pane pane = new Pane();
                pane.setLayoutX(ticketQuestions.getPhoto() != null ? 654 : 0);
                pane.setLayoutY(value);
                pane.setPrefWidth(ticketQuestions.getPhoto() != null ? 462 : 811);
                pane.setPrefHeight(text.getLayoutBounds().getHeight() + 26);
                pane.setStyle("-fx-background-color: #141414; -fx-background-radius: 25px");

                Label label = new Label(ticketAnswersEntity.getText());
                label.setWrapText(true);
                label.setLayoutX(25);
                label.setLayoutY(6);
                label.setPrefWidth(text.getLayoutBounds().getWidth());
                label.setPrefHeight(text.getLayoutBounds().getHeight() + 15);
                label.setTextFill(
                        Paint.valueOf(
                                ticketQuestions.getCorrect() == ticketAnswersEntity.getNumber() ? (ticketQuestions.getAnswer() != null ? "#078029" : "#dab55e") :
                                ticketQuestions.getCorrect() != ticketQuestions.getAnswer() && ticketQuestions.getAnswer() == ticketAnswersEntity.getNumber() ?
                                        "#ae1b1b" : "#909090"));
                label.setFont(Font.font("Times New Roman", FontWeight.BOLD, 16));
                pane.getChildren().add(label);

                value += pane.getPrefHeight() + 10;
                paneTicket.getChildren().add(pane);
            }

            Pane paneBack = new Pane();
            paneBack.setPrefWidth(132);
            paneBack.setPrefHeight(44);
            paneBack.setStyle("-fx-background-color: #141414");
            paneBack.setVisible(question != 0);

            Label labelBack = new Label("« Назад");
            labelBack.setLayoutX(26);
            labelBack.setLayoutY(11);
            labelBack.setPrefWidth(76);
            labelBack.setPrefHeight(23);
            labelBack.setFont(Font.font("Monospaced", FontWeight.BOLD, 18));
            labelBack.setTextFill(Paint.valueOf("#9e9e9e"));
            paneBack.getChildren().add(labelBack);

            Pane paneNext = new Pane();
            paneNext.setPrefWidth(132);
            paneNext.setPrefHeight(44);
            paneNext.setStyle("-fx-background-color: #141414");

            Label labelNext = new Label("Далее »");
            labelNext.setLayoutX(26);
            labelNext.setLayoutY(11);
            labelNext.setPrefWidth(76);
            labelNext.setPrefHeight(23);
            labelNext.setFont(Font.font("Monospaced", FontWeight.BOLD, 18));
            labelNext.setTextFill(Paint.valueOf("#9e9e9e"));
            paneNext.setVisible(question != 39);
            paneNext.getChildren().add(labelNext);

            if(ticketQuestions.getPhoto() != null) {

                ImageView imageView = new ImageView(new Image(ticketQuestions.getPhoto()));
                imageView.setLayoutX(0);
                imageView.setLayoutY(0);
                imageView.setFitWidth(635);
                imageView.setFitHeight(446);
                paneTicket.getChildren().add(imageView);

            }

            paneBack.setLayoutY(value + 10);
            paneNext.setLayoutY(value + 10);

            paneBack.setLayoutX(ticketQuestions.getPhoto() != null ? 654 : 0);
            paneNext.setLayoutX(paneBack.isVisible() ?
                    (ticketQuestions.getPhoto() != null ? paneBack.getLayoutX() + 325 : paneBack.getLayoutX() + 675) :
                    ticketQuestions.getPhoto() != null ? 654 : 0);
            paneTicket.getChildren().addAll(paneBack, paneNext);

            paneBack.setOnMouseEntered(event -> {
                paneBack.setStyle("-fx-background-color: #121212");
                paneBack.setLayoutY(paneBack.getLayoutY() - 1);
            });
            paneBack.setOnMouseExited(event -> {
                paneBack.setStyle("-fx-background-color: #141414");
                paneBack.setLayoutY(paneBack.getLayoutY() + 1);
            });
            paneBack.setOnMouseClicked(event -> {
                drawQuestion(question - 1);
            });

            paneNext.setOnMouseEntered(event -> {
                paneNext.setStyle("-fx-background-color: #121212");
                paneNext.setLayoutY(paneNext.getLayoutY() - 1);
            });
            paneNext.setOnMouseExited(event -> {
                paneNext.setStyle("-fx-background-color: #141414");
                paneNext.setLayoutY(paneNext.getLayoutY() + 1);
            });
            paneNext.setOnMouseClicked(event -> {
                drawQuestion(question + 1);
            });

            Text textCorrect = new Text("Правильный ответ, вариант №" + ticketQuestions.getCorrect());
            textCorrect.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));

            Text textExplanation = new Text(ticketQuestions.getExplanation());
            textExplanation.setFont(Font.font("Times New Roman", FontWeight.BOLD, 18));
            textExplanation.setWrappingWidth(1119);

            Label labelCorrect = new Label("Правильный ответ, вариант №" + ticketQuestions.getCorrect());
            labelCorrect.setLayoutX(14);
            labelCorrect.setLayoutY(ticketQuestions.getPhoto() != null ? 490 : paneNext.getLayoutY() + 75);
            labelCorrect.setPrefWidth(textCorrect.getLayoutBounds().getWidth());
            labelCorrect.setPrefHeight(textCorrect.getLayoutBounds().getHeight());
            labelCorrect.setTextFill(Paint.valueOf("#908d8d"));
            labelCorrect.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));

            Label labelExplanation = new Label(ticketQuestions.getExplanation());
            labelExplanation.setWrapText(true);
            labelExplanation.setLayoutX(14);
            labelExplanation.setLayoutY(labelCorrect.getLayoutY() + 30);
            labelExplanation.setPrefWidth(textExplanation.getLayoutBounds().getWidth());
            labelExplanation.setPrefHeight(textExplanation.getLayoutBounds().getHeight() + 15);
            labelExplanation.setTextFill(Paint.valueOf("#908d8d"));
            labelExplanation.setFont(Font.font("Times New Roman", FontWeight.BOLD, 18));

            paneTicket.getChildren().addAll(labelCorrect, labelExplanation);

        }

    }

    public class TaskTimer extends TimerTask {

        private long time;

        public TaskTimer(){}
        public TaskTimer(Date dateStart, Date dateEnd){

             time = (dateEnd.getTime() - dateStart.getTime()) / 1000;

        }

        @Override
        public void run() {
            Platform.runLater(() ->{

                long minutes = time / 60;
                long seconds = time % 60;

                labelTicketTime.setText((minutes < 10 ? "0" + minutes : minutes) + ":" + (seconds < 10 ? "0" + seconds : seconds));
                time--;

                if(minutes == 0 && seconds == 0){

                    new CloseTicket();
                    groupCloseMenuTicket.setVisible(false);
                    groupTicketResult.setVisible(true);

                }

            });
        }

    }

    public class NoConnect{

        public NoConnect(){

            if(timerRetry != null){
                timerRetry.cancel();
                timerRetry = null;
            }
            timerRetry = new Timer();
            timerRetry.schedule(new TaskTimerRetry(30), 1000, 1000);

            groupNoConnect.setVisible(true);

            buttonRetryConnect.setOnMouseEntered(event -> {
                buttonRetryConnect.setLayoutY(buttonRetryConnect.getLayoutY() - 1);
                buttonRetryConnect.setStyle("-fx-background-color: #080808");
            });

            buttonRetryConnect.setOnMouseExited(event -> {
                buttonRetryConnect.setLayoutY(buttonRetryConnect.getLayoutY() + 1);
                buttonRetryConnect.setStyle("-fx-background-color: #101010");
            });

            buttonRetryConnect.setOnMouseClicked(event -> {

                boolean connect = API.pingHost();
                groupNoConnect.setVisible(!connect);
                if(connect){
                    if(timerRetry != null){
                        timerRetry.cancel();
                        timerRetry = null;
                    }
                    countdownRetry = 0;
                }

            });

        }

    }

    public class TaskTimerRetry extends TimerTask {

        public TaskTimerRetry(){}
        public TaskTimerRetry(long time){
            countdownRetry = time;
        }

        @Override
        public void run() {
            Platform.runLater(() ->{

                if(countdownRetry-- <= 0){

                    boolean connect = API.pingHost();
                    groupNoConnect.setVisible(!connect);
                    if(connect){

                        if(LobbyController.NoConnect.init){
                            initialize();
                        }

                        if(timerRetry != null){
                            timerRetry.cancel();
                            timerRetry = null;
                        }

                        countdownRetry = 0;
                        return;
                    }
                    countdownRetry = 30;
                }
                labelRetryConnect.setText("Повторная попытка через " + countdownRetry + " секунд.");

            });
        }

    }

}
