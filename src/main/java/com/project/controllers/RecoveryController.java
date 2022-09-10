package com.project.controllers;

import com.project.API;
import com.project.Runner;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class RecoveryController extends Application {

    @FXML
    private Label labelError;

    @FXML
    private Pane paneRecovery;

    @FXML
    private Label labelSendCode;

    @FXML
    private Label labelWaitCode;

    @FXML
    private TextField textfieldLogin;

    @FXML
    private ImageView imageClose;

    @FXML
    private ImageView imageCollapse;

    @FXML
    private ImageView imageBack;

    @FXML
    private Button btnRecovery;

    @FXML
    private TextField textfieldCode;

    @FXML
    private PasswordField passwordfieldNew;

    @FXML
    private PasswordField passwordfieldConfirm;

    private double offsetPosX;
    private double offsetPosY;

    public static Timer timer;
    public static String input;
    private static long countdown;

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Runner.class.getResource("scene/recovery.fxml"));
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

        labelError.setVisible(false);

        new StartWithSave();

        new ActionPanel();
        new ActionWithField();
        new SendCode();
        new RecoveryButton();
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
            backOnMouseClicked();

        }

        public void eventOnMouseEntered(){

            imageCollapse.setOnMouseEntered(event -> imageCollapse.setOpacity(1.0));
            imageClose.setOnMouseEntered(event -> imageClose.setOpacity(1.0));
            imageBack.setOnMouseEntered(event -> imageBack.setOpacity(1.0));

        }

        public void eventOnMouseExit(){

            imageCollapse.setOnMouseExited(event -> imageCollapse.setOpacity(0.5));
            imageClose.setOnMouseExited(event -> imageClose.setOpacity(0.5));
            imageBack.setOnMouseExited(event -> imageBack.setOpacity(0.5));

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

        public void backOnMouseClicked(){

            imageBack.setOnMouseClicked(event -> {

                Stage stage = (Stage) imageBack.getScene().getWindow();
                //stage.close();

                try {
                    new AuthController().start(stage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });

        }

    }

    public class SendCode{

        public SendCode(){

            textfieldLogin.textProperty().addListener(((observableValue, oldValue, newValue) -> {

                input = newValue;

                if(!newValue.matches("^[a-zA-Z0-9]+$") &&
                        !newValue.matches("^[-\\w.]+@([A-z0-9][-A-z0-9]+\\.)+[A-z]{2,4}$")){

                    textfieldLogin.setStyle("-fx-background-color: #141414; -fx-text-fill: #807c7c; -fx-border-color: #6b2525");
                    labelError.setText("Неверный формат!");
                    labelError.setVisible(true);
                    return;

                }
                textfieldLogin.setStyle("-fx-background-color: #141414; -fx-text-fill: #807c7c");
                labelError.setVisible(false);

            }));

            labelSendCode.setOnMouseEntered(event -> labelSendCode.setTextFill(Paint.valueOf("#4a4848")));
            labelSendCode.setOnMouseExited(event -> labelSendCode.setTextFill(Paint.valueOf("#807c7c")));

            labelSendCode.setOnMouseClicked(event -> {

                if(textfieldLogin.getText() == null || (!textfieldLogin.getText().matches("^[a-zA-Z0-9]+$") &&
                        !textfieldLogin.getText().matches("^[-\\w.]+@([A-z0-9][-A-z0-9]+\\.)+[A-z]{2,4}$"))){

                    labelError.setText("Введите логин или E-mail!");
                    textfieldLogin.setStyle("-fx-background-color: #141414; -fx-text-fill: #807c7c; -fx-border-color: #6b2525");
                    labelError.setVisible(true);
                    return;

                }

                String status = new API().sendCodeRecovery(textfieldLogin.getText());
                if(status.isEmpty() || status.equals("User with given data not found!")){

                    labelError.setText("Аккаунт не найден!");
                    textfieldLogin.setStyle("-fx-background-color: #141414; -fx-text-fill: #807c7c; -fx-border-color: #6b2525");
                    labelError.setVisible(true);
                    return;

                }

                if(timer != null){
                    timer.cancel();
                    timer = null;
                }
                timer = new Timer();
                timer.schedule(new TaskTimer(30), 1000, 1000);

                textfieldLogin.setEditable(false);
                btnRecovery.setDisable(false);
                labelSendCode.setVisible(false);
                labelWaitCode.setVisible(true);
                paneRecovery.setDisable(false);

            });
        }
    }

    public class ActionWithField{

        public ActionWithField(){

            actionLogin();
            actionCode();
            actionNewPassword();
            actionConfirmPassword();

        }

        public void actionLogin(){

            textfieldLogin.textProperty().addListener(((observableValue, oldValue, newValue) -> {

                if(!newValue.matches("^[a-zA-Z0-9]+$")){

                    textfieldLogin.setStyle("-fx-background-color: #141414; -fx-text-fill: #807c7c; -fx-border-color: #6b2525");
                    labelError.setText("Неверный формат логина!");
                    labelError.setVisible(true);
                    return;

                }
                textfieldLogin.setStyle("-fx-background-color: #141414; -fx-text-fill: #807c7c");
                labelError.setVisible(false);

            }));

        }

        public void actionCode(){

            textfieldCode.textProperty().addListener(((observableValue, oldValue, newValue) -> {

                if(newValue.length() != 6){

                    textfieldCode.setStyle("-fx-background-color: #141414; -fx-text-fill: #807c7c; -fx-border-color: #6b2525");
                    labelError.setText("Неверный формат кода!");
                    labelError.setVisible(true);
                    return;

                }
                textfieldCode.setStyle("-fx-background-color: #141414; -fx-text-fill: #807c7c");
                labelError.setVisible(false);

            }));

        }

        public void actionNewPassword(){

            passwordfieldNew.textProperty().addListener(((observableValue, oldValue, newValue) -> {

                if(!newValue.matches("(?=^.{6,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$")){

                    passwordfieldNew.setStyle("-fx-background-color: #141414; -fx-text-fill: #807c7c; -fx-border-color: #6b2525");
                    passwordfieldConfirm.setStyle("-fx-background-color: #141414; -fx-text-fill: #807c7c; -fx-border-color: #6b2525");
                    labelError.setText("Неверный формат пароля!");
                    labelError.setVisible(true);
                    return;

                }
                passwordfieldConfirm.setStyle("-fx-background-color: #141414; -fx-text-fill: #807c7c");
                passwordfieldNew.setStyle("-fx-background-color: #141414; -fx-text-fill: #807c7c");
                labelError.setVisible(false);

            }));

        }

        public void actionConfirmPassword(){

            passwordfieldConfirm.textProperty().addListener(((observableValue, oldValue, newValue) -> {

                String newPassword = passwordfieldNew.getText();
                if(!newValue.matches("(?=^.{6,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$") || !newPassword.equals(newValue)){

                    passwordfieldNew.setStyle("-fx-background-color: #141414; -fx-text-fill: #807c7c; -fx-border-color: #6b2525");
                    passwordfieldConfirm.setStyle("-fx-background-color: #141414; -fx-text-fill: #807c7c; -fx-border-color: #6b2525");
                    labelError.setText("Не совпадают пароли!");
                    labelError.setVisible(true);
                    return;

                }
                passwordfieldConfirm.setStyle("-fx-background-color: #141414; -fx-text-fill: #807c7c");
                passwordfieldNew.setStyle("-fx-background-color: #141414; -fx-text-fill: #807c7c");
                labelError.setVisible(false);

            }));

        }

    }

    public class RecoveryButton{

        public RecoveryButton(){

            btnRecovery.setOnMouseEntered(event -> {
                btnRecovery.setStyle("-fx-background-color: #121212; -fx-background-radius: 25px");
                btnRecovery.setLayoutY(btnRecovery.getLayoutY() - 1);
            });
            btnRecovery.setOnMouseExited(event -> {
                btnRecovery.setStyle("-fx-background-color: #141414; -fx-background-radius: 25px");
                btnRecovery.setLayoutY(btnRecovery.getLayoutY() + 1);
            });
            btnRecovery.setOnMouseClicked(event -> {

                if(textfieldCode.getText().length() != 6){

                    labelError.setText("Неверный формат кода!");
                    labelError.setVisible(true);

                    textfieldCode.setStyle("-fx-background-color: #141414; -fx-text-fill: #807c7c; -fx-border-color: #6b2525");
                    return;

                }

                if(!passwordfieldNew.getText().matches("(?=^.{6,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$")){

                    passwordfieldNew.setStyle("-fx-background-color: #141414; -fx-text-fill: #807c7c; -fx-border-color: #6b2525");
                    passwordfieldConfirm.setStyle("-fx-background-color: #141414; -fx-text-fill: #807c7c; -fx-border-color: #6b2525");
                    labelError.setText("Неверный формат пароля!");
                    labelError.setVisible(true);
                    return;

                }

                if(!passwordfieldNew.getText().equals(passwordfieldConfirm.getText())){

                    passwordfieldNew.setStyle("-fx-background-color: #141414; -fx-text-fill: #807c7c; -fx-border-color: #6b2525");
                    passwordfieldConfirm.setStyle("-fx-background-color: #141414; -fx-text-fill: #807c7c; -fx-border-color: #6b2525");
                    labelError.setText("Не совпадают пароли!");
                    labelError.setVisible(true);
                    return;

                }

                String status = new API().changePassword(textfieldLogin.getText(), textfieldCode.getText(), passwordfieldNew.getText());
                if(status.equals("Invalid code entered!")){

                    labelError.setText("Не верный код!");
                    labelError.setVisible(true);
                    textfieldCode.setStyle("-fx-background-color: #141414; -fx-text-fill: #807c7c; -fx-border-color: #6b2525");
                    return;

                }

                if(status.equals("Code time is up!")){

                    labelError.setText("Время кода истекло!");
                    labelError.setVisible(true);
                    textfieldCode.setStyle("-fx-background-color: #141414; -fx-text-fill: #807c7c; -fx-border-color: #6b2525");
                    return;

                }

                if(status.equals("Your password has been changed!")) {
                    labelError.setText("Пароль изменён!");
                    labelError.setVisible(true);
                }
            });

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

                labelWaitCode.setText("Запросить код можно через " + countdown + " сек.");

                if(countdown-- <= 0){

                    labelWaitCode.setVisible(false);
                    labelSendCode.setVisible(true);

                }

            });
        }

    }

    public class StartWithSave{

        public StartWithSave(){

            textfieldLogin.setText(input);
            if(countdown > 0){

                labelWaitCode.setText("Запросить код можно через " + countdown + " сек.");

                paneRecovery.setDisable(false);
                btnRecovery.setDisable(false);
                textfieldLogin.setEditable(false);
                labelWaitCode.setVisible(true);
                labelSendCode.setVisible(false);

                if(timer != null){
                    timer.cancel();
                    timer = null;
                }
                timer = new Timer();
                timer.schedule(new TaskTimer(countdown), 1000, 1000);
            }

        }

    }

}
