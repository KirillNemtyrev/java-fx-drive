package com.project;

import com.project.controllers.AuthController;
import com.project.controllers.LobbyController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;

public class Runner extends Application {

    @FXML
    private Button buttonRetryConnect;

    @FXML
    private ImageView imageClose;

    @FXML
    private ImageView imageCollapse;

    @FXML
    private Label labelRetryConnect;

    private static long countdownRetry;

    private static Timer timerRetry;

    @Override
    public void start(Stage stage) throws IOException, URISyntaxException {

        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        stage.setTitle("ПДД РК");
        stage.getIcons().add(new Image(Runner.class.getResource("images/rules/rules.png").toURI().toString()));

        new Config();
        if(!API.pingHost()){

            FXMLLoader fxmlLoader = new FXMLLoader(Runner.class.getResource("scene/noconnect.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 346, 539);
            stage.setScene(scene);
            stage.show();
            return;
        }

        if(Config.token != null && new API().checkToken(Config.token)){
            new LobbyController(Config.token).start(stage);
            return;
        }
        new AuthController().start(stage);
    }

    @FXML
    public void initialize(){

        imageCollapse.setOnMouseEntered(event -> imageCollapse.setOpacity(1.0));
        imageClose.setOnMouseEntered(event -> imageClose.setOpacity(1.0));

        imageCollapse.setOnMouseExited(event -> imageCollapse.setOpacity(0.5));
        imageClose.setOnMouseExited(event -> imageClose.setOpacity(0.5));

        imageCollapse.setOnMouseClicked(event -> {
            Stage stage = (Stage) imageCollapse.getScene().getWindow();
            stage.setIconified(true);
        });

        imageClose.setOnMouseClicked(event -> {
            Stage stage = (Stage) imageClose.getScene().getWindow();
            stage.close();
        });

        if(timerRetry != null){
            timerRetry.cancel();
            timerRetry = null;
        }
        timerRetry = new Timer();
        timerRetry.schedule(new TaskTimerRetry(30), 1000, 1000);

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
            if(connect){
                if(timerRetry != null){
                    timerRetry.cancel();
                    timerRetry = null;
                }
                countdownRetry = 0;

                Stage stage = (Stage) buttonRetryConnect.getScene().getWindow();
                stage.close();
                if(Config.token != null && new API().checkToken(Config.token)){
                    try {
                        new LobbyController(Config.token).start(stage);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return;
                }
                try {
                    new AuthController().start(stage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        });

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
                    if(connect){
                        if(timerRetry != null){
                            timerRetry.cancel();
                            timerRetry = null;
                        }
                        countdownRetry = 0;

                        Stage stage = (Stage) buttonRetryConnect.getScene().getWindow();
                        stage.close();
                        if(Config.token != null && new API().checkToken(Config.token)){
                            try {
                                new LobbyController(Config.token).start(stage);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            return;
                        }
                        try {
                            new AuthController().start(stage);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        return;
                    }
                    countdownRetry = 30;
                }
                labelRetryConnect.setText("Повторная попытка через " + countdownRetry + " секунд.");

            });
        }

    }

    public static void main(String[] args){
        launch();
    }

}
