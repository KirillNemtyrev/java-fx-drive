package com.project.controllers;

import com.project.Runner;
import com.project.draw.TicketNumbers;
import com.project.entity.TicketAnswersEntity;
import com.project.entity.TicketEntity;
import com.project.entity.TicketQuestionsEntity;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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
import java.util.ArrayList;
import java.util.List;

public class TicketController extends Application {

    /* Variables for FXML top panel */
    @FXML
    private ImageView imageCollapse;

    @FXML
    private ImageView imageClose;

    /* Variables for FXML ticket questions */
    @FXML
    private ScrollPane scrollPaneQuestions;

    @FXML
    private AnchorPane anchorPaneQuestions;

    @FXML
    private Label labelNumberQuestion;

    @FXML
    private Label labelTextQuestion;

    @FXML
    private Pane paneTicket;

    @FXML
    private Label labelAnswered;


    private double offsetPosX;
    private double offsetPosY;

    private static TicketEntity ticketEntity;

    public TicketController(){}
    public TicketController(TicketEntity ticketEntity){
        this.ticketEntity = ticketEntity;
    }

    public void setTicketEntity(TicketEntity ticketEntity){
        this.ticketEntity = ticketEntity;
    }

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Runner.class.getResource("scene/ticket.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 920);
        stage.setResizable(false);
        stage.setTitle("Alpha Test");
        stage.initStyle(StageStyle.UNDECORATED);
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
        private Label labelChoise;

        /**
         * The function is used to draw ticket numbers.
         * @param count
         */
        public TicketQuestions(int count){

            for(int temp = 0; temp < count; temp++){

                Pane pane = new Pane();
                pane.setLayoutX(0);
                pane.setLayoutY(54*temp);
                pane.setPrefWidth(69);
                pane.setPrefHeight(54);
                pane.setStyle("-fx-background-color: #141414");

                int number = temp;
                pane.setOnMouseEntered(event -> pane.setStyle("-fx-background-color: #111111"));
                pane.setOnMouseExited(event -> pane.setStyle("-fx-background-color: #141414"));
                pane.setOnMouseClicked(event -> selectQuestion(number));
                anchorPaneQuestions.getChildren().add(pane);

                Label label = new Label(String.valueOf(temp + 1));
                label.setLayoutX(15);
                label.setLayoutY(15);
                label.setPrefWidth(40);
                label.setPrefHeight(17);
                label.setAlignment(Pos.CENTER);
                label.setFont(Font.font("Consolas", FontWeight.BOLD, 18));
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

            labelChoise = null;

            TicketQuestionsEntity ticketQuestions = ticketEntity.getTicket(question);

            labelNumberQuestion.setText("Вопрос #" + (question + 1));
            labelTextQuestion.setText(ticketQuestions.getText());

            paneTicket.getChildren().clear();

            int value = 5;
            for(TicketAnswersEntity ticketAnswersEntity : ticketQuestions.getAnswers()){

                Text text = new Text(ticketAnswersEntity.getText());
                text.setWrappingWidth(ticketQuestions.getPhoto() != null ? 416 : 765);
                text.setFont(Font.font("Consolas", FontWeight.BOLD, 16));

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
                label.setFont(Font.font("Consolas", FontWeight.BOLD, 16));
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
                    getAnswered();
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
        public void getAnswered(){

            int ammount = 0;
            for(int count = 0; count < answers.length; count++){

                if(answers[count] == null){
                    continue;
                }

                ammount++;
            }

            labelAnswered.setText(ammount + "/40");
        }

    }

}
