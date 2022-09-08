package com.project.controllers;

import com.project.API;
import com.project.Runner;
import com.project.entity.StatisticEntity;
import com.project.entity.TicketEntity;
import com.project.entity.TicketHistoryEntity;
import eu.hansolo.tilesfx.events.BoundsEventListener;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class LobbyController extends Application {

    @FXML
    private AnchorPane anchorPaneHistory;

    @FXML
    private Button buttonChangeEmail;

    @FXML
    private Button buttonChangePassword;

    @FXML
    private Button buttonChangePhoto;

    @FXML
    private Circle circlePhoto;

    @FXML
    private Circle circlePhotoSettings;

    @FXML
    private Group groupSettings;

    @FXML
    private ImageView imageClose;

    @FXML
    private ImageView imageCloseSettings;

    @FXML
    private ImageView imageCollapse;

    @FXML
    private ImageView imageSettingsChangeLogin;

    @FXML
    private ImageView imageSettingsChangeName;

    @FXML
    private Label labelDateSubscribe;

    @FXML
    private Label labelErrorSettings;

    @FXML
    private Label labelName;

    @FXML
    private Label labelRecoveryData;

    @FXML
    private Label labelSendCodeOld;

    @FXML
    private Label labelSettingsWaitOldEmail;

    @FXML
    private Label labelStatisticProbality;

    @FXML
    private Label labelStatisticResolved;

    @FXML
    private Label labelStatisticTotal;

    @FXML
    private Label labelStatisticUnDelivery;

    @FXML
    private Label labelStatisticUnResolved;

    @FXML
    private Label labelVersion;

    @FXML
    private Pane paneBackgroundSettings;

    @FXML
    private Pane paneExitAccount;

    @FXML
    private Pane paneMain;

    @FXML
    private Pane panePanel;

    @FXML
    private Group groupNeedSubscribe;

    @FXML
    private Pane paneSettings;

    @FXML
    private Pane paneStartTicket;

    @FXML
    private Pane paneStatistic;

    @FXML
    private Pane paneSubscribe;

    @FXML
    private Pane paneTicket;

    @FXML
    private PasswordField passwordSettingsConfirm;

    @FXML
    private PasswordField passwordSettingsNew;

    @FXML
    private PasswordField passwordSettingsOld;

    @FXML
    private PieChart pieChartStatistic;

    @FXML
    private ScrollPane scrollPaneHistory;

    @FXML
    private TextField textfieldCodeOld;

    @FXML
    private TextField textfieldNewEmail;

    @FXML
    private TextField textfieldOldEmail;

    @FXML
    private TextField textfieldSettingsLogin;

    @FXML
    private TextField textfieldSettingsName;

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
        new Settings();
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

            exitAccountOnMouseClicked();
            collapseOnMouseClicked();
            closeOnMouseClicked();

        }

        public void eventOnMouseEntered(){

            imageCollapse.setOnMouseEntered(event -> imageCollapse.setOpacity(1.0));
            imageClose.setOnMouseEntered(event -> imageClose.setOpacity(1.0));
            paneExitAccount.setOnMouseEntered(event -> paneExitAccount.setOpacity(1.0));

        }

        public void eventOnMouseExit(){

            imageCollapse.setOnMouseExited(event -> imageCollapse.setOpacity(0.5));
            imageClose.setOnMouseExited(event -> imageClose.setOpacity(0.5));
            paneExitAccount.setOnMouseExited(event -> paneExitAccount.setOpacity(0.75));

        }

        public void exitAccountOnMouseClicked(){

            paneExitAccount.setOnMouseClicked(event -> {
                Stage stage = (Stage) paneExitAccount.getScene().getWindow();
                stage.close();

                try {
                    new AuthController().start(stage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

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

            paneSubscribe.setVisible(statisticEntity.isSubscribe());
            paneTicket.setDisable(!statisticEntity.isSubscribe());
            paneTicket.setOpacity(statisticEntity.isSubscribe() ? 1.0 : 0.25);
            groupNeedSubscribe.setVisible(!statisticEntity.isSubscribe());

            if(statisticEntity.isSubscribe()){

                String time = new SimpleDateFormat("dd MMMM yyyy", new Locale("ru", "RU")).format(statisticEntity.getSubscribeEnd());
                String clock = new SimpleDateFormat("HH:mm", new Locale("ru", "RU")).format(statisticEntity.getSubscribeEnd());

                labelDateSubscribe.setText(time + " г. в " + clock);
            }

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
            pieChartNotPass.getNode().setStyle("-fx-font: 18 Consolas; -fx-pie-color: #570303; -fx-fill: #8f8f8f");
            pieChartNotEnd.getNode().setStyle("-fx-font: 18 Consolas; -fx-pie-color: #dab55e; -fx-fill: #8f8f8f");

            circlePhoto.setFill(new ImagePattern(new Image(statisticEntity.getPhoto())));
            circlePhotoSettings.setFill(new ImagePattern(new Image(statisticEntity.getPhoto())));
            labelName.setText(statisticEntity.getName());

            textfieldSettingsLogin.setText(statisticEntity.getUsername());
            textfieldSettingsLogin.setPromptText(statisticEntity.getUsername());

            textfieldSettingsName.setText(statisticEntity.getName());
            textfieldSettingsName.setPromptText(statisticEntity.getName());

            textfieldOldEmail.setText(statisticEntity.getEmail());
            textfieldOldEmail.setEditable(false);

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

                Label date = new Label(time + " г. в " + clock);
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
                paneStartTicket.setStyle("-fx-background-color: #080808");
            });

            paneStartTicket.setOnMouseExited(event -> {
                paneStartTicket.setLayoutY(paneStartTicket.getLayoutY() + 1);
                paneStartTicket.setStyle("-fx-background-color: #101010");
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

    public class Settings{

        public Settings(){

            paneSettings.setOnMouseEntered(event -> {
                paneSettings.setLayoutY(paneSettings.getLayoutY() - 1);
                paneSettings.setStyle("-fx-background-color: #080808; -fx-background-radius: 25px");
            });

            paneSettings.setOnMouseExited(event -> {
                paneSettings.setLayoutY(paneSettings.getLayoutY() + 1);
                paneSettings.setStyle("-fx-background-color: #101010; -fx-background-radius: 25px");
            });

            paneSettings.setOnMouseClicked(event -> groupSettings.setVisible(true));

            imageCloseSettings.setOnMouseEntered(event -> imageCloseSettings.setOpacity(1.0));
            imageCloseSettings.setOnMouseExited(event -> imageCloseSettings.setOpacity(0.5));
            imageCloseSettings.setOnMouseClicked(event -> groupSettings.setVisible(false));
            paneBackgroundSettings.setOnMouseClicked(event -> groupSettings.setVisible(false));

            changeAvatar();
            changeUsername();
            changeName();
            changePassword();
        }

        public void changeAvatar(){

            buttonChangePhoto.setOnMouseEntered(event -> {
                buttonChangePhoto.setLayoutY(buttonChangePhoto.getLayoutY() - 1);
                buttonChangePhoto.setStyle("-fx-background-color: #080808; -fx-background-radius: 25px");
            });

            buttonChangePhoto.setOnMouseExited(event -> {
                buttonChangePhoto.setLayoutY(buttonChangePhoto.getLayoutY() + 1);
                buttonChangePhoto.setStyle("-fx-background-color: #101010; -fx-background-radius: 25px");
            });

            buttonChangePhoto.setOnMouseClicked(event -> {

                FileChooser.ExtensionFilter imageFilter
                        = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");

                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(imageFilter);
                fileChooser.setTitle("Сменить фотографию");

                File file = fileChooser.showOpenDialog(buttonChangePhoto.getScene().getWindow());
                Boolean result = api.changeAvatar(file);
                if(result){

                    circlePhotoSettings.setFill(new ImagePattern(new Image(file.toURI().toString())));
                    circlePhoto.setFill(new ImagePattern(new Image(file.toURI().toString())));

                }

            });


        }

        public void changeUsername(){

            textfieldSettingsLogin.textProperty().addListener((observableValue, oldValue, newValue) -> {
                textfieldSettingsLogin.setStyle("-fx-background-color: #111111; -fx-text-fill: #c3baba" +
                        (newValue.length() < 6 || newValue.length() > 40 || !newValue.matches("^[a-zA-Z0-9]+$") ? ";-fx-border-color: #6b2525" : ""));
            });

            imageSettingsChangeLogin.setOnMouseEntered(event -> {
                if(textfieldSettingsLogin.getText().length() < 6 || textfieldSettingsLogin.getText().length() > 40 ||
                        !textfieldSettingsLogin.getText().matches("^[a-zA-Z0-9]+$") ||
                        textfieldSettingsLogin.getText().equals(textfieldSettingsLogin.getPromptText()))
                    return;

                imageSettingsChangeLogin.setOpacity(1.0);
            });
            imageSettingsChangeLogin.setOnMouseExited(event -> imageSettingsChangeLogin.setOpacity(0.5));

            imageSettingsChangeLogin.setOnMouseClicked(event -> {

                if(textfieldSettingsLogin.getText().length() < 6 || textfieldSettingsLogin.getText().length() > 40 ||
                        !textfieldSettingsLogin.getText().matches("^[a-zA-Z0-9]+$") ||
                        textfieldSettingsLogin.getText().equals(textfieldSettingsLogin.getPromptText()))
                    return;

                Boolean result = api.changeUsername(textfieldSettingsLogin.getText());

                if(result){
                    textfieldSettingsLogin.setPromptText(textfieldSettingsLogin.getText());

                    TranslateTransition transition = new TranslateTransition();
                    transition.setDuration(new Duration(70));
                    transition.setByX(48);
                    transition.setToX(10);
                    transition.setCycleCount(4);
                    transition.setNode(textfieldSettingsLogin);
                    transition.setAutoReverse(true);
                    transition.playFromStart();
                    return;
                }

                textfieldSettingsLogin.setStyle("-fx-background-color: #111111; -fx-text-fill: #c3baba;-fx-border-color: #6b2525");
                labelErrorSettings.setText("Данный логин существует!");

            });

        }

        public void changeName(){

            textfieldSettingsName.textProperty().addListener((observableValue, oldValue, newValue) -> {
                textfieldSettingsName.setStyle("-fx-background-color: #111111; -fx-text-fill: #c3baba" +
                        (newValue.length() < 6 || newValue.length() > 40 ? ";-fx-border-color: #6b2525" : ""));
            });

            imageSettingsChangeName.setOnMouseEntered(event -> {
                if(textfieldSettingsName.getText().length() < 6 || textfieldSettingsName.getText().length() > 40 ||
                        textfieldSettingsName.getText().equals(textfieldSettingsName.getPromptText()))
                    return;

                imageSettingsChangeName.setOpacity(1.0);
            });
            imageSettingsChangeName.setOnMouseExited(event -> imageSettingsChangeName.setOpacity(0.5));

            imageSettingsChangeName.setOnMouseClicked(event -> {

                if(textfieldSettingsName.getText().length() < 6 || textfieldSettingsName.getText().length() > 40 ||
                        textfieldSettingsName.getText().equals(textfieldSettingsName.getPromptText()))
                    return;

                Boolean result = api.changeName(textfieldSettingsName.getText());

                if(result){
                    textfieldSettingsName.setPromptText(textfieldSettingsName.getText());
                    labelName.setText(textfieldSettingsName.getText());

                    TranslateTransition transition = new TranslateTransition();
                    transition.setDuration(new Duration(70));
                    transition.setByX(48);
                    transition.setToX(10);
                    transition.setCycleCount(4);
                    transition.setNode(textfieldSettingsName);
                    transition.setAutoReverse(true);
                    transition.playFromStart();
                    return;
                }

                textfieldSettingsName.setStyle("-fx-background-color: #111111; -fx-text-fill: #c3baba;-fx-border-color: #6b2525");
                labelErrorSettings.setText("Произошла ошибка!");

            });

        }

        public void changePassword(){

            passwordSettingsOld.textProperty().addListener((observableValue, oldValue, newValue) -> {
                passwordSettingsOld.setStyle("-fx-background-color: #111111; -fx-text-fill: #c3baba" +
                        (!newValue.matches("(?=^.{6,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$") ? ";-fx-border-color: #6b2525" : ""));
            });

            passwordSettingsNew.textProperty().addListener((observableValue, oldValue, newValue) -> {
                if(!newValue.matches("(?=^.{6,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$") ||
                        !newValue.equals(passwordSettingsConfirm.getText())){

                    passwordSettingsNew.setStyle("-fx-background-color: #111111; -fx-text-fill: #c3baba;-fx-border-color: #6b2525");
                    passwordSettingsConfirm.setStyle("-fx-background-color: #111111; -fx-text-fill: #c3baba;-fx-border-color: #6b2525");
                    return;
                }
                passwordSettingsNew.setStyle("-fx-background-color: #111111; -fx-text-fill: #c3baba");
                passwordSettingsConfirm.setStyle("-fx-background-color: #111111; -fx-text-fill: #c3baba");
            });

            passwordSettingsConfirm.textProperty().addListener((observableValue, oldValue, newValue) -> {
                if(!newValue.matches("(?=^.{6,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$") ||
                        !newValue.equals(passwordSettingsConfirm.getText())){

                    passwordSettingsNew.setStyle("-fx-background-color: #111111; -fx-text-fill: #c3baba;-fx-border-color: #6b2525");
                    passwordSettingsConfirm.setStyle("-fx-background-color: #111111; -fx-text-fill: #c3baba;-fx-border-color: #6b2525");
                    return;
                }
                passwordSettingsNew.setStyle("-fx-background-color: #111111; -fx-text-fill: #c3baba");
                passwordSettingsConfirm.setStyle("-fx-background-color: #111111; -fx-text-fill: #c3baba");
            });

            buttonChangePassword.setOnMouseEntered(event -> {
                buttonChangePassword.setLayoutY(buttonChangePassword.getLayoutY() - 1);
                buttonChangePassword.setStyle("-fx-background-color: #080808; -fx-background-radius: 25px");
            });

            buttonChangePassword.setOnMouseExited(event -> {
                buttonChangePassword.setLayoutY(buttonChangePassword.getLayoutY() + 1);
                buttonChangePassword.setStyle("-fx-background-color: #101010; -fx-background-radius: 25px");
            });

            buttonChangePassword.setOnMouseClicked(event -> {

                if(!passwordSettingsOld.getText().matches("(?=^.{6,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$") ||
                        !passwordSettingsNew.getText().matches("(?=^.{6,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$") ||
                        !passwordSettingsNew.getText().equals(passwordSettingsConfirm.getText()))
                    return;

                if(api.changePassword(passwordSettingsOld.getText(), passwordSettingsNew.getText())) {
                    passwordSettingsOld.clear();
                    passwordSettingsNew.clear();
                    passwordSettingsConfirm.clear();

                    passwordSettingsOld.setStyle("-fx-background-color: #111111; -fx-text-fill: #c3baba");
                    passwordSettingsNew.setStyle("-fx-background-color: #111111; -fx-text-fill: #c3baba");
                    passwordSettingsConfirm.setStyle("-fx-background-color: #111111; -fx-text-fill: #c3baba");

                    labelErrorSettings.setText("Пароль изменён!");
                    TranslateTransition transition = new TranslateTransition();
                    transition.setDuration(new Duration(70));
                    transition.setByX(48);
                    transition.setToX(10);
                    transition.setCycleCount(4);
                    transition.setNode(buttonChangePassword);
                    transition.setAutoReverse(true);
                    transition.playFromStart();
                    return;

                }

                labelErrorSettings.setText("Неверный пароль!");
                passwordSettingsOld.setStyle("-fx-background-color: #111111; -fx-text-fill: #c3baba;-fx-border-color: #6b2525");
            });

        }

    }

}
