package com.project.controllers;

import com.project.API;
import com.project.Config;
import com.project.Runner;
import com.project.entity.AuthEntity;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class AuthController extends Application {

    @FXML
    private Button btnAuth;

    @FXML
    private Label labelRecovery;

    @FXML
    private Button btnRegister;

    @FXML
    private Button btnSupport;

    @FXML
    private ImageView imageClose;

    @FXML
    private ImageView imageCollapse;

    @FXML
    private Pane paneMain;

    @FXML
    private Pane panePanel;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField textfieldLogin;

    @FXML
    private Pane paneAuth;

    @FXML
    private Label labelError;

    private double offsetPosX;
    private double offsetPosY;

    private boolean timeoutToken = false;

    public AuthController(){}
    public AuthController(boolean timeoutToken){
        this.timeoutToken = timeoutToken;
    }

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Runner.class.getResource("scene/auth.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 346, 539);
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

    @FXML
    public void initialize() {

        labelError.setVisible(timeoutToken);
        labelError.setText(timeoutToken ? "Время сессии истекло!" : "Ошибка авторизации!");

        new ActionPanel();
        new ActionBtn();
        new ActionWithField();

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

    public class ActionBtn{

        public ActionBtn(){
            ActionAuthBtn();
            ActionRegisterBtn();
            ActionSupportBtn();
            ActionRecoveryLabel();
        }

        public void ActionAuthBtn() {
            btnAuth.setOnMouseEntered(event -> btnAuth.setStyle("-fx-background-color: #121212; -fx-background-radius: 25px"));
            btnAuth.setOnMouseExited(event -> btnAuth.setStyle("-fx-background-color: #141414; -fx-background-radius: 25px"));

            btnAuth.setOnMouseClicked(event -> new Authentication());
        }

        public void ActionRegisterBtn() {
            btnRegister.setOnMouseEntered(event -> btnRegister.setStyle("-fx-background-color: #121212; -fx-background-radius: 25px"));
            btnRegister.setOnMouseExited(event -> btnRegister.setStyle("-fx-background-color: #141414; -fx-background-radius: 25px"));

            btnRegister.setOnMouseClicked(event -> {

                Stage stage = (Stage) btnRegister.getScene().getWindow();
                //stage.close();

                try {
                    new RegisterController().start(stage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });
        }

        public void ActionSupportBtn() {
            btnSupport.setOnMouseEntered(event -> btnSupport.setStyle("-fx-background-color: #121212; -fx-background-radius: 25px"));
            btnSupport.setOnMouseExited(event -> btnSupport.setStyle("-fx-background-color: #141414; -fx-background-radius: 25px"));
        }

        public void ActionRecoveryLabel(){

            labelRecovery.setOnMouseEntered(event -> labelRecovery.setTextFill(Paint.valueOf("#5e5d5d")));
            labelRecovery.setOnMouseExited(event -> labelRecovery.setTextFill(Paint.valueOf("#807c7c")));
            labelRecovery.setOnMouseClicked(event -> {

                Stage stage = (Stage) labelRecovery.getScene().getWindow();
                try {
                    new RecoveryController().start(stage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

        }
    }

    public class ActionWithField{

        public ActionWithField(){

            textfieldLogin.textProperty().addListener((observableValue, s, t1) -> {

                labelError.setVisible(false);
                textfieldLogin.setStyle("-fx-background-color: #141414; -fx-text-fill: #807c7c");
                passwordField.setStyle("-fx-background-color: #141414; -fx-text-fill: #807c7c");

            });

            passwordField.textProperty().addListener((observableValue, s, t1) -> {

                labelError.setVisible(false);
                textfieldLogin.setStyle("-fx-background-color: #141414; -fx-text-fill: #807c7c");
                passwordField.setStyle("-fx-background-color: #141414; -fx-text-fill: #807c7c");

            });

            textfieldLogin.setOnKeyPressed(event -> {

                if(event.getCode() == KeyCode.ENTER) {
                    new Authentication();
                }

            });
            passwordField.setOnKeyPressed(event -> {

                if(event.getCode() == KeyCode.ENTER) {
                    new Authentication();
                }

            });

        }

    }

    public class Authentication{

        public Authentication(){

            if(textfieldLogin.getText().trim().isEmpty() || passwordField.getText().trim().isEmpty()){

                labelError.setVisible(true);
                labelError.setText("Заполните все поля!");
                return;

            }

            AuthEntity authEntity = new API().authProfile(textfieldLogin.getText().trim(), passwordField.getText().trim());
            if(authEntity == null){

                labelError.setVisible(true);
                labelError.setText("Неверные данные!");

                textfieldLogin.setStyle("-fx-background-color: #141414; -fx-text-fill: #807c7c; -fx-border-color: #6b2525");
                passwordField.setStyle("-fx-background-color: #141414; -fx-text-fill: #807c7c; -fx-border-color: #6b2525");

                TranslateTransition transition = new TranslateTransition();
                transition.setDuration(new Duration(70));
                transition.setByX(48);
                transition.setToX(10);
                transition.setCycleCount(4);
                transition.setNode(paneAuth);
                transition.setAutoReverse(true);
                transition.playFromStart();

                return;
            }

            Stage stage = (Stage) btnAuth.getScene().getWindow();
            stage.close();
            try {
                new LobbyController(authEntity.getAccessToken()).start(stage);
                Config.token = authEntity.getAccessToken();
                Config.save();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

    }
}
