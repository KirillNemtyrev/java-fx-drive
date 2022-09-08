package com.project.controllers;

import com.project.API;
import com.project.Runner;
import com.project.entity.AuthEntity;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.IOException;

public class RegisterController extends Application {

    @FXML
    private ImageView imageBack;

    @FXML
    private ImageView imageCollapse;

    @FXML
    private ImageView imageClose;

    @FXML
    private Label labelError;

    @FXML
    private TextField textfieldLogin;

    @FXML
    private TextField textfieldName;

    @FXML
    private TextField textfieldEmail;

    @FXML
    private PasswordField passwordfieldNew;

    @FXML
    private PasswordField passwordfieldConfirm;

    @FXML
    private Button btnRegister;

    private double offsetPosX;
    private double offsetPosY;

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Runner.class.getResource("scene/register.fxml"));
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

        new ActionPanel();
        new ActionWithField();
        new ActionWithBtn();

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

    public class ActionWithField{

        public ActionWithField(){

            actionLogin();
            actionName();
            actionEmail();
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

        public void actionName(){

            textfieldName.textProperty().addListener(((observableValue, oldValue, newValue) -> {

                if(newValue.length() < 6 || newValue.length() > 40 ){

                    textfieldName.setStyle("-fx-background-color: #141414; -fx-text-fill: #807c7c; -fx-border-color: #6b2525");
                    labelError.setText("Кол-во символов имени от 6 до 40!");
                    labelError.setVisible(true);
                    return;

                }
                textfieldName.setStyle("-fx-background-color: #141414; -fx-text-fill: #807c7c");
                labelError.setVisible(false);

            }));

        }

        public void actionEmail(){

            textfieldEmail.textProperty().addListener(((observableValue, oldValue, newValue) -> {

                if(!newValue.matches("^[-\\w.]+@([A-z0-9][-A-z0-9]+\\.)+[A-z]{2,4}$")){

                    textfieldEmail.setStyle("-fx-background-color: #141414; -fx-text-fill: #807c7c; -fx-border-color: #6b2525");
                    labelError.setText("Неверный формат E-mail адреса!");
                    labelError.setVisible(true);
                    return;

                }
                textfieldEmail.setStyle("-fx-background-color: #141414; -fx-text-fill: #807c7c");
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

    public class ActionWithBtn{

        public ActionWithBtn(){

            btnRegister.setOnMouseEntered(event -> {
                btnRegister.setStyle("-fx-background-color: #121212; -fx-background-radius: 25px");
                btnRegister.setLayoutY(btnRegister.getLayoutY() - 1);
            });
            btnRegister.setOnMouseExited(event -> {
                btnRegister.setStyle("-fx-background-color: #141414; -fx-background-radius: 25px");
                btnRegister.setLayoutY(btnRegister.getLayoutY() + 1);
            });
            btnRegister.setOnMouseClicked(event -> {
                try {
                    register();
                } catch (IOException e) {
                    labelError.setText("Произошла ошибка!");
                    labelError.setVisible(true);
                }
            });

        }

        public void register() throws IOException {

            if(!textfieldLogin.getText().matches("^[a-zA-Z0-9]+$")){

                textfieldLogin.setStyle("-fx-background-color: #141414; -fx-text-fill: #807c7c; -fx-border-color: #6b2525");
                labelError.setText("Неверный формат логина!");
                labelError.setVisible(true);
                return;

            }

            if(textfieldName.getText().length() < 6 || textfieldName.getText().length() > 40 ){

                textfieldName.setStyle("-fx-background-color: #141414; -fx-text-fill: #807c7c; -fx-border-color: #6b2525");
                labelError.setText("Кол-во символов имени от 6 до 40!");
                labelError.setVisible(true);
                return;

            }

            if(!textfieldEmail.getText().matches("^[-\\w.]+@([A-z0-9][-A-z0-9]+\\.)+[A-z]{2,4}$")){

                textfieldEmail.setStyle("-fx-background-color: #141414; -fx-text-fill: #807c7c; -fx-border-color: #6b2525");
                labelError.setText("Неверный формат E-mail адреса!");
                labelError.setVisible(true);
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

            String status = new API().registerProfile(textfieldLogin.getText(), textfieldName.getText(), textfieldEmail.getText(), passwordfieldNew.getText());
            if(status.equals("Username is already taken!")){

                labelError.setText("Данный логин уже занят!");
                textfieldLogin.setStyle("-fx-background-color: #141414; -fx-text-fill: #807c7c; -fx-border-color: #6b2525");
                labelError.setVisible(true);
                return;

            }

            if(status.equals("Email Address already in use!")){

                labelError.setText("Данный E-mail уже занят!");
                textfieldEmail.setStyle("-fx-background-color: #141414; -fx-text-fill: #807c7c; -fx-border-color: #6b2525");
                labelError.setVisible(true);
                return;

            }

            AuthEntity authEntity = new API().authProfile(textfieldLogin.getText(), passwordfieldConfirm.getText());
            Stage stage = (Stage) btnRegister.getScene().getWindow();
            stage.close();
            new LobbyController(authEntity.getAccessToken()).start(stage);

        }

    }

}
