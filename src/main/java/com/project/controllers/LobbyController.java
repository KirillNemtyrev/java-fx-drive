package com.project.controllers;

import com.project.API;
import com.project.Runner;
import com.project.entity.StatisticEntity;
import com.project.entity.SubscribeEntity;
import com.project.entity.TicketEntity;
import com.project.entity.TicketHistoryEntity;
import eu.hansolo.tilesfx.events.BoundsEventListener;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
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

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;

public class LobbyController extends Application {

    @FXML
    private AnchorPane anchorPaneHistory;

    @FXML
    private AnchorPane anchorPaneSubscribes;

    @FXML
    private Button buttonChangeEmail;

    @FXML
    private Button buttonChangePassword;

    @FXML
    private Button buttonChangePhoto;

    @FXML
    private Button buttonCheckCode;

    @FXML
    private Button buttonRetryClose;

    @FXML
    private Button buttonRetryConnect;

    @FXML
    private Button buttonRetryStart;

    @FXML
    private Circle circlePhoto;

    @FXML
    private Circle circlePhotoSettings;

    @FXML
    private Group groupHistorySubscribe;

    @FXML
    private Group groupNeedSubscribe;

    @FXML
    private Group groupNoConnect;

    @FXML
    private Group groupRetryTicket;

    @FXML
    private Group groupSettings;

    @FXML
    private ImageView imageClose;

    @FXML
    private ImageView imageCloseRetry;

    @FXML
    private ImageView imageCloseSettings;

    @FXML
    private ImageView imageCloseSubscribe;

    @FXML
    private ImageView imageCollapse;

    @FXML
    private ImageView imageSettingsChangeLogin;

    @FXML
    private ImageView imageSettingsChangeName;

    @FXML
    private Label labelDateHistorySubscribe;

    @FXML
    private Label labelDateSubscribe;

    @FXML
    private Label labelErrorSettings;

    @FXML
    private Button buttonBuySubscribe;

    @FXML
    private Label labelName;

    @FXML
    private Label labelResultPromo;

    @FXML
    private Label labelRetryAttempts;

    @FXML
    private Label labelRetryConnect;

    @FXML
    private Label labelRetryDate;

    @FXML
    private Label labelRetryStatus;

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
    private Label labelZeroStatistic;

    @FXML
    private Pane paneBackgroundRetry;

    @FXML
    private Pane paneBackgroundSettings;

    @FXML
    private Pane paneBackgroundSubscribe;

    @FXML
    private Pane paneExitAccount;

    @FXML
    private Pane paneForPie;

    @FXML
    private Pane paneMain;

    @FXML
    private Pane panePanel;

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
    private TextField textfieldCode;

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


    public static Timer timer;
    public static Timer timerRetry;
    private static long countdown;
    private static long countdownRetry;

    private double offsetPosX;
    private double offsetPosY;

    private static API api = new API();

    private Date dateSubscribe = null;

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

        if(!API.pingHost()){
            new NoConnect(true);
            return;
        }
        new Settings();
        new Statistic();
        new Ticket();
        new Subscribe();

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

            dateSubscribe = null;

            paneTicket.setDisable(!statisticEntity.isSubscribe());
            paneTicket.setOpacity(statisticEntity.isSubscribe() ? 1.0 : 0.25);
            groupNeedSubscribe.setVisible(!statisticEntity.isSubscribe());

            labelDateSubscribe.setText("Подписка отсутствует.");
            if(statisticEntity.isSubscribe()){

                dateSubscribe = statisticEntity.getSubscribeEnd();

                String time = new SimpleDateFormat("dd MMMM yyyy", new Locale("ru", "RU")).format(statisticEntity.getSubscribeEnd());
                String clock = new SimpleDateFormat("HH:mm", new Locale("ru", "RU")).format(statisticEntity.getSubscribeEnd());

                labelDateSubscribe.setText(time + " г. в " + clock);
            }

            labelDateHistorySubscribe.setText(labelDateSubscribe.getText());
            labelStatisticTotal.setText(String.valueOf(statisticEntity.getTicket().getTotal()));
            labelStatisticResolved.setText(String.valueOf(statisticEntity.getTicket().getResolved()));
            labelStatisticUnResolved.setText(String.valueOf(statisticEntity.getTicket().getUnresolved()));
            labelStatisticUnDelivery.setText(String.valueOf(statisticEntity.getTicket().getUndelivered()));
            labelStatisticProbality.setText(String.format("%(.2f%%", statisticEntity.getTicket().getProbability()));

            pieChartStatistic.getData().clear();
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

                labelZeroStatistic.setVisible(true);
                //paneForPie.setStyle("-fx-background-color: gray; -fx-background-radius: 150px");
                return;
            }

            paneForPie.setVisible(false);
            labelZeroStatistic.setVisible(false);
            //paneForPie.setStyle("-fx-background-color: white; -fx-background-radius: 150px");
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

                if(statisticEntity.isSubscribe()){

                    pane.setOnMouseEntered(event -> pane.setStyle("-fx-background-color: #121212"));
                    pane.setOnMouseExited(event -> pane.setStyle("-fx-background-color: #141414"));
                    pane.setOnMouseClicked(event -> {

                        if(!API.pingHost()){
                            new NoConnect();
                            return;
                        }

                        if(dateSubscribe != null && dateSubscribe.before(new Date())){
                            new Statistic();
                            return;
                        }

                        labelRetryDate.setText("Тест от " + time + " г. в " + clock);
                        labelRetryStatus.setText("Статус: " + (ticketHistoryEntity.getTicketResultStatus().equals("TICKET_NOT_END") ? "Не отправлен (" :
                                ticketHistoryEntity.getTicketResultStatus().equals("TICKET_PASSED") ? "Сдал (" : "Не сдал (") + ticketHistoryEntity.getCorrect() + "/40)");
                        labelRetryAttempts.setText("Попыток: "+  ticketHistoryEntity.getAttempts());

                        groupRetryTicket.setVisible(true);

                        imageCloseRetry.setOnMouseEntered(mouseEvent -> imageCloseRetry.setOpacity(1.0));
                        imageCloseRetry.setOnMouseExited(mouseEvent -> imageCloseRetry.setOpacity(0.5));
                        imageCloseRetry.setOnMouseClicked(mouseEvent -> groupRetryTicket.setVisible(false));
                        paneBackgroundRetry.setOnMouseClicked(mouseEvent -> groupRetryTicket.setVisible(false));

                        buttonRetryStart.setOnMouseEntered(mouseEvent -> {
                            buttonRetryStart.setLayoutY(buttonRetryStart.getLayoutY() - 1);
                            buttonRetryStart.setStyle("-fx-background-color: #080808");
                        });
                        buttonRetryStart.setOnMouseExited(mouseEvent -> {
                            buttonRetryStart.setLayoutY(buttonRetryStart.getLayoutY() + 1);
                            buttonRetryStart.setStyle("-fx-background-color: #101010");
                        });
                        buttonRetryStart.setOnMouseClicked(mouseEvent -> {

                            if(!API.pingHost()){
                                new NoConnect();
                                return;
                            }

                            if(dateSubscribe != null && dateSubscribe.before(new Date())){
                                new Statistic();
                                return;
                            }

                            Stage stage = (Stage) paneStartTicket.getScene().getWindow();
                            stage.close();
                            try {
                                new TicketController(api.getToken(), ticketHistoryEntity.getUuid()).start(stage);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                        });


                        buttonRetryClose.setOnMouseEntered(mouseEvent -> {
                            buttonRetryClose.setLayoutY(buttonRetryClose.getLayoutY() - 1);
                            buttonRetryClose.setStyle("-fx-background-color: #080808");
                        });
                        buttonRetryClose.setOnMouseExited(mouseEvent -> {
                            buttonRetryClose.setLayoutY(buttonRetryClose.getLayoutY() + 1);
                            buttonRetryClose.setStyle("-fx-background-color: #101010");
                        });
                        buttonRetryClose.setOnMouseClicked(mouseEvent -> groupRetryTicket.setVisible(false));

                    });

                }
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

                if(!API.pingHost()){
                    new NoConnect();
                    return;
                }

                if(dateSubscribe != null && dateSubscribe.before(new Date())){
                    new Statistic();
                    return;
                }

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
            textfieldNewEmail.setDisable(true);
            textfieldCodeOld.setDisable(true);
            buttonChangeEmail.setDisable(true);

            paneSettings.setOnMouseEntered(event -> {
                paneSettings.setLayoutY(paneSettings.getLayoutY() - 1);
                paneSettings.setStyle("-fx-background-color: #080808; -fx-background-radius: 25px");
            });

            paneSettings.setOnMouseExited(event -> {
                paneSettings.setLayoutY(paneSettings.getLayoutY() + 1);
                paneSettings.setStyle("-fx-background-color: #101010; -fx-background-radius: 25px");
            });

            paneSettings.setOnMouseClicked(event -> {

                if(!API.pingHost()){
                    new NoConnect();
                    return;
                }

                if(dateSubscribe != null && dateSubscribe.before(new Date())){
                    new Statistic();
                    return;
                }

                groupSettings.setVisible(true);
            });

            imageCloseSettings.setOnMouseEntered(event -> imageCloseSettings.setOpacity(1.0));
            imageCloseSettings.setOnMouseExited(event -> imageCloseSettings.setOpacity(0.5));
            imageCloseSettings.setOnMouseClicked(event -> groupSettings.setVisible(false));
            paneBackgroundSettings.setOnMouseClicked(event -> groupSettings.setVisible(false));

            changeAvatar();
            changeUsername();
            changeName();
            changePassword();
            changeEmail();
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

                if(!API.pingHost()){
                    new NoConnect();
                    return;
                }

                FileChooser.ExtensionFilter imageFilter
                        = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");

                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(imageFilter);
                fileChooser.setTitle("Сменить фотографию");

                File file = fileChooser.showOpenDialog(buttonChangePhoto.getScene().getWindow());

                if(!API.pingHost()){
                    new NoConnect();
                    return;
                }
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

                if(!API.pingHost()){
                    new NoConnect();
                    return;
                }

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

                if(!API.pingHost()){
                    new NoConnect();
                    return;
                }

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

                if(!API.pingHost()){
                    new NoConnect();
                    return;
                }

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

        public void changeEmail(){

            labelSendCodeOld.setOnMouseEntered(event -> labelSendCodeOld.setTextFill(Paint.valueOf("#726c6c")));
            labelSendCodeOld.setOnMouseExited(event -> labelSendCodeOld.setTextFill(Paint.valueOf("#c3baba")));
            labelSendCodeOld.setOnMouseClicked(event -> {

                if(!API.pingHost()){
                    new NoConnect();
                    return;
                }

                if(api.sendCodeToChangeEmail()){

                    labelSendCodeOld.setVisible(false);
                    labelSettingsWaitOldEmail.setVisible(true);

                    textfieldCodeOld.setDisable(false);
                    textfieldNewEmail.setDisable(false);
                    buttonChangeEmail.setDisable(false);

                    if(timer != null){
                        timer.cancel();
                        timer = null;
                    }
                    timer = new Timer();
                    timer.schedule(new TaskTimer(30), 1000, 1000);
                    return;
                }

                labelErrorSettings.setText("Произошла ошибка!");
                labelSendCodeOld.setTextFill(Paint.valueOf("#8a1f1f"));
            });

            textfieldNewEmail.textProperty().addListener((observableValue, oldValue, newValue) -> {
                if(!newValue.matches("^[-\\w.]+@([A-z0-9][-A-z0-9]+\\.)+[A-z]{2,4}$")){

                    textfieldNewEmail.setStyle("-fx-background-color: #111111; -fx-text-fill: #c3baba;-fx-border-color: #6b2525");
                    return;
                }
                textfieldNewEmail.setStyle("-fx-background-color: #111111; -fx-text-fill: #c3baba");
            });

            textfieldCodeOld.textProperty().addListener((observableValue, oldValue, newValue) -> {
                if(newValue.length() != 6){

                    textfieldCodeOld.setStyle("-fx-background-color: #111111; -fx-text-fill: #c3baba;-fx-border-color: #6b2525");
                    return;
                }
                textfieldCodeOld.setStyle("-fx-background-color: #111111; -fx-text-fill: #c3baba");
            });


            buttonChangeEmail.setOnMouseEntered(event -> {
                buttonChangeEmail.setLayoutY(buttonChangeEmail.getLayoutY() - 1);
                buttonChangeEmail.setStyle("-fx-background-color: #080808; -fx-background-radius: 25px");
            });

            buttonChangeEmail.setOnMouseExited(event -> {
                buttonChangeEmail.setLayoutY(buttonChangeEmail.getLayoutY() + 1);
                buttonChangeEmail.setStyle("-fx-background-color: #101010; -fx-background-radius: 25px");
            });

            buttonChangeEmail.setOnMouseClicked(event -> {

                if(!API.pingHost()){
                    new NoConnect();
                    return;
                }

                if(textfieldCodeOld.getText().length() != 6){

                    textfieldCodeOld.setStyle("-fx-background-color: #111111; -fx-text-fill: #c3baba;-fx-border-color: #6b2525");
                    return;

                }

                if(!textfieldNewEmail.getText().matches("^[-\\w.]+@([A-z0-9][-A-z0-9]+\\.)+[A-z]{2,4}$")){

                    textfieldNewEmail.setStyle("-fx-background-color: #111111; -fx-text-fill: #c3baba;-fx-border-color: #6b2525");
                    return;
                }

                String result = api.changeEmail(textfieldCodeOld.getText(), textfieldNewEmail.getText());
                switch (result){
                    case "Your email changed!":
                        textfieldOldEmail.setText(textfieldNewEmail.getText());
                        TranslateTransition transition = new TranslateTransition();
                        transition.setDuration(new Duration(70));
                        transition.setByX(48);
                        transition.setToX(10);
                        transition.setCycleCount(4);
                        transition.setNode(textfieldOldEmail);
                        transition.setAutoReverse(true);
                        transition.playFromStart();

                        labelErrorSettings.setText("Основные настройки");
                        textfieldCodeOld.clear();
                        textfieldNewEmail.clear();

                        textfieldNewEmail.setStyle("-fx-background-color: #111111; -fx-text-fill: #c3baba");
                        textfieldCodeOld.setStyle("-fx-background-color: #111111; -fx-text-fill: #c3baba");

                        buttonChangeEmail.setStyle("-fx-background-color: #080808; -fx-background-radius: 25px; -fx-border-color: #078029; -fx-border-radius: 25px");

                        textfieldOldEmail.setDisable(true);
                        textfieldNewEmail.setDisable(true);
                        buttonChangeEmail.setDisable(true);
                        labelSendCodeOld.setVisible(true);
                        labelSettingsWaitOldEmail.setVisible(false);

                        if(timer != null){
                            timer.cancel();
                            timer = null;
                        }
                        break;
                    case "This email already!":
                        textfieldNewEmail.setStyle("-fx-background-color: #111111; -fx-text-fill: #c3baba;-fx-border-color: #6b2525");
                        labelErrorSettings.setText("E-mail уже используется!");
                        break;
                    default:
                        textfieldCodeOld.setStyle("-fx-background-color: #111111; -fx-text-fill: #c3baba;-fx-border-color: #6b2525");
                        labelErrorSettings.setText("Код недействителен!");
                        break;
                }

            });



        }
    }

    public class Subscribe{

        public Subscribe(){

            buttonBuySubscribe.setOnMouseEntered(event -> {
                buttonBuySubscribe.setLayoutY(buttonBuySubscribe.getLayoutY() - 1);
                buttonBuySubscribe.setStyle("-fx-background-color: #080808; -fx-background-radius: 25px");
            });

            buttonBuySubscribe.setOnMouseExited(event -> {
                buttonBuySubscribe.setLayoutY(buttonBuySubscribe.getLayoutY() + 1);
                buttonBuySubscribe.setStyle("-fx-background-color: #101010; -fx-background-radius: 25px");
            });

            buttonBuySubscribe.setOnMouseClicked(event -> {

                if(!API.pingHost()){
                    new NoConnect();
                    return;
                }

                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    try {
                        Desktop.getDesktop().browse(new URI("https://t.me/japanverblud"));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }

            });

            paneSubscribe.setOnMouseEntered(event -> {
                paneSubscribe.setLayoutY(paneSubscribe.getLayoutY() - 1);
                paneSubscribe.setStyle("-fx-background-color: #101010; -fx-background-radius: 25px");
            });

            paneSubscribe.setOnMouseExited(event -> {
                paneSubscribe.setLayoutY(paneSubscribe.getLayoutY() + 1);
                paneSubscribe.setStyle("-fx-background-color: #141414; -fx-background-radius: 25px");
            });

            paneSubscribe.setOnMouseClicked(event -> {

                if(!API.pingHost()){
                    new NoConnect();
                    return;
                }

                if(dateSubscribe != null && dateSubscribe.before(new Date())){
                    new Statistic();
                    return;
                }

                subscribeHistory();

                groupHistorySubscribe.setVisible(true);
            });

            imageCloseSubscribe.setOnMouseEntered(event -> imageCloseSubscribe.setOpacity(1.0));
            imageCloseSubscribe.setOnMouseExited(event -> imageCloseSubscribe.setOpacity(0.5));
            imageCloseSubscribe.setOnMouseClicked(event -> groupHistorySubscribe.setVisible(false));
            paneBackgroundSubscribe.setOnMouseClicked(event -> groupHistorySubscribe.setVisible(false));

            subscribeActivate();

        }

        public void subscribeActivate(){

            textfieldCode.textProperty().addListener((observableValue, oldValue, newValue) -> {
                textfieldCode.setStyle("-fx-background-color: #101010; -fx-text-fill: #aba7a7");
                labelResultPromo.setVisible(false);
            });


            buttonCheckCode.setOnMouseEntered(event -> {
                buttonCheckCode.setLayoutY(buttonCheckCode.getLayoutY() - 1);
                buttonCheckCode.setStyle("-fx-background-color: #080808");
            });

            buttonCheckCode.setOnMouseExited(event -> {
                buttonCheckCode.setLayoutY(buttonCheckCode.getLayoutY() + 1);
                buttonCheckCode.setStyle("-fx-background-color: #101010");
            });

            buttonCheckCode.setOnMouseClicked(event -> {

                if(!API.pingHost()){
                    new NoConnect();
                    return;
                }

                if(dateSubscribe != null && dateSubscribe.before(new Date())){
                    new Statistic();
                    return;
                }

                labelResultPromo.setVisible(true);
                switch (api.activateCode(textfieldCode.getText())){
                    case "This code is activated!":
                        subscribeHistory();
                        updateDateSubscribe();

                        labelResultPromo.setText("Промокод активирован!");
                        textfieldCode.setStyle("-fx-background-color: #101010; -fx-text-fill: #aba7a7;-fx-border-color: #078029");
                        break;
                    case "You have already used this promo code!":
                        labelResultPromo.setText("Вы уже использовали данный промокод!");
                        textfieldCode.setStyle("-fx-background-color: #101010; -fx-text-fill: #aba7a7;-fx-border-color: #6b2525");
                        break;
                    case "The number of uses of this promotional code has been used up!":
                        labelResultPromo.setText("Данный промокод уже не действителен!");
                        textfieldCode.setStyle("-fx-background-color: #101010; -fx-text-fill: #aba7a7;-fx-border-color: #6b2525");
                        break;
                    default:
                        labelResultPromo.setText("Данный промокод не найден!");
                        textfieldCode.setStyle("-fx-background-color: #101010; -fx-text-fill: #aba7a7;-fx-border-color: #6b2525");
                        break;
                }

            });

        }

        public void updateDateSubscribe(){
            dateSubscribe = api.getDateSubscribe();
            String date = new SimpleDateFormat("dd MMMM yyyy", new Locale("ru", "RU")).format(dateSubscribe);
            String clock = new SimpleDateFormat("HH:mm", new Locale("ru", "RU")).format(dateSubscribe);
            String time = date + " г. в " + clock;

            labelDateSubscribe.setText(time);
            labelDateHistorySubscribe.setText(time);

            paneTicket.setDisable(false);
            paneTicket.setOpacity(1.0);
            groupNeedSubscribe.setVisible(false);
        }

        public void subscribeHistory(){

            anchorPaneSubscribes.getChildren().clear();

            List<SubscribeEntity> subscribeEntities = api.getSubscribes();
            if(subscribeEntities == null || subscribeEntities.size() == 0){

                ImageView imageView = new ImageView();
                imageView.setLayoutX(60);
                imageView.setLayoutY(30);
                imageView.setFitWidth(280);
                imageView.setFitHeight(284);
                imageView.setImage(new Image(Runner.class.getResource("images/history/handy-machine-learning.gif").toString()));

                Label title = new Label("Ваша история подписок пуста..");
                title.setLayoutX(72);
                title.setLayoutY(290);
                title.setPrefWidth(256);
                title.setPrefHeight(20);
                title.setAlignment(Pos.CENTER);
                title.setFont(Font.font("Times New Roman", FontWeight.BOLD, 18));
                title.setTextFill(Paint.valueOf("#9e9e9e"));

                Label description = new Label("Используйте промокод для получения, продления подписки");
                description.setLayoutX(24);
                description.setLayoutY(310);
                description.setPrefWidth(348);
                description.setPrefHeight(41);
                description.setTextAlignment(TextAlignment.CENTER);
                description.setFont(Font.font("Times New Roman", FontWeight.BOLD, 16));
                description.setTextFill(Paint.valueOf("#797777"));
                description.setWrapText(true);

                anchorPaneSubscribes.getChildren().addAll(imageView, title, description);
                anchorPaneSubscribes.setPrefHeight(452);
                return;
            }

            Collections.sort(subscribeEntities, new Comparator<SubscribeEntity>() {
                @Override
                public int compare(SubscribeEntity h, SubscribeEntity v) {
                    return v.getDate().compareTo(h.getDate());
                }
            });

            long value = 0;
            for(SubscribeEntity subscribeEntity : subscribeEntities){

                Pane pane = new Pane();
                pane.setLayoutX(14);
                pane.setLayoutY(value * 45);
                pane.setPrefWidth(370);
                pane.setPrefHeight(41);
                pane.setStyle("-fx-background-color: #101010");

                Label code = new Label(subscribeEntity.getCode());
                code.setLayoutX(2);
                code.setLayoutY(6);
                code.setPrefWidth(160);
                code.setPrefHeight(16);
                code.setTextFill(Paint.valueOf("#aba7a7"));
                code.setFont(Font.font("Times New Roman", FontWeight.BOLD, 14));
                code.setAlignment(Pos.CENTER);

                Label descriptionCode = new Label("использованный код");
                descriptionCode.setLayoutX(30);
                descriptionCode.setLayoutY(19);
                descriptionCode.setPrefWidth(101);
                descriptionCode.setPrefHeight(16);
                descriptionCode.setTextFill(Paint.valueOf("#727070"));
                descriptionCode.setFont(Font.font("Times New Roman", FontWeight.BOLD, 10));

                String time = new SimpleDateFormat("dd.MM.yyyy", new Locale("ru", "RU")).format(subscribeEntity.getDate());
                String clock = new SimpleDateFormat("HH:mm", new Locale("ru", "RU")).format(subscribeEntity.getDate());

                Label date = new Label(time + " в " + clock);
                date.setLayoutX(166);
                date.setLayoutY(6);
                date.setPrefWidth(113);
                date.setPrefHeight(16);
                date.setTextFill(Paint.valueOf("#aba7a7"));
                date.setFont(Font.font("Times New Roman", FontWeight.BOLD, 14));
                date.setAlignment(Pos.CENTER);

                Label descriptionDate = new Label("дата использования");
                descriptionDate.setLayoutX(174);
                descriptionDate.setLayoutY(19);
                descriptionDate.setPrefWidth(101);
                descriptionDate.setPrefHeight(16);
                descriptionDate.setTextFill(Paint.valueOf("#727070"));
                descriptionDate.setFont(Font.font("Times New Roman", FontWeight.BOLD, 10));

                Label ammount = new Label(String.valueOf(subscribeEntity.getValue()));
                ammount.setLayoutX(295);
                ammount.setLayoutY(6);
                ammount.setPrefWidth(63);
                ammount.setPrefHeight(16);
                ammount.setTextFill(Paint.valueOf("#aba7a7"));
                ammount.setFont(Font.font("Times New Roman", FontWeight.BOLD, 14));
                ammount.setAlignment(Pos.CENTER);

                Label descriptionAmmount = new Label("кол-во дней");
                descriptionAmmount.setLayoutX(299);
                descriptionAmmount.setLayoutY(19);
                descriptionAmmount.setPrefWidth(59);
                descriptionAmmount.setPrefHeight(16);
                descriptionAmmount.setTextFill(Paint.valueOf("#727070"));
                descriptionAmmount.setFont(Font.font("Times New Roman", FontWeight.BOLD, 10));

                pane.getChildren().addAll(code, descriptionCode, date, descriptionDate, ammount, descriptionAmmount);
                anchorPaneSubscribes.getChildren().add(pane);
                value++;
            }
            anchorPaneSubscribes.setPrefHeight(Math.max(value*45, 452));

        }

    }

    public class TaskTimer extends TimerTask {

        public TaskTimer(){}
        public TaskTimer(long time){
            countdown = time;
        }

        @Override
        public void run() {
            Platform.runLater(() ->{

                if(countdown-- <= 0){

                    labelSettingsWaitOldEmail.setVisible(false);
                    labelSendCodeOld.setVisible(true);

                }
                labelSettingsWaitOldEmail.setText("Повторная отправка через " + countdown + " сек.");

            });
        }

    }

    public class NoConnect{

        public static boolean init;

        public NoConnect(boolean start){

            init = start;
            new NoConnect();

            buttonRetryConnect.setOnMouseClicked(event -> {

                boolean connect = API.pingHost();
                groupNoConnect.setVisible(!connect);
                if(connect){
                    initialize();
                    if(timerRetry != null){
                        timerRetry.cancel();
                        timerRetry = null;
                    }
                    countdownRetry = 0;
                }

            });
        }

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

                        if(NoConnect.init){
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
