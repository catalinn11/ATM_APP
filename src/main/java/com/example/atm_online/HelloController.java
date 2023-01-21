package com.example.atm_online;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class HelloController implements Initializable {
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;
    @FXML
    private ImageView minImage;
    @FXML
    private ImageView expImage;
    @FXML
    private ImageView closeImage;
    @FXML
    private Pane titleBar;
    @FXML
    private TextField user;
    @FXML
    private PasswordField pass;
    @FXML
    private Label loginMessage;
    @FXML
    private ImageView bankicon;
    private double xOffset = 0, yOffset = 0;
    protected static String currentUser;

    public EventHandler<KeyEvent> enterPress = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent keyEvent) {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                try {
                    loginButtonHandle();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        titleBar.setOnMouseClicked(event -> {
            Stage stage = (Stage) titleBar.getScene().getWindow();
            xOffset = stage.getX() - event.getScreenX();
            yOffset = stage.getY() - event.getScreenY();
        });

        titleBar.setOnMouseDragged(event -> {
            Stage stage = (Stage) titleBar.getScene().getWindow();
            stage.setX(event.getScreenX() + xOffset);
            stage.setY(event.getScreenY() + yOffset);
        });

        minImage.setOnMouseClicked(event -> {
            Stage stage = (Stage) minImage.getScene().getWindow();
            stage.setIconified(true);
        });

        expImage.setOnMouseClicked(event -> {
            Stage stage = (Stage) expImage.getScene().getWindow();
            if (stage.isMaximized())
                stage.setMaximized(false);
            else
                stage.setMaximized(true);
        });

        closeImage.setOnMouseClicked(event -> {
            Stage stage = (Stage) closeImage.getScene().getWindow();
            stage.close();
        });

        bankicon.setImage(new Image("icon.png"));
        user.setOnKeyPressed(enterPress);
        pass.setOnKeyPressed(enterPress);
    }

    public void loginButtonHandle() throws SQLException, IOException {
        if (user.getText().isEmpty() || pass.getText().isEmpty()) {
            loginMessage.setText("Please enter username and password!");

        }
        else {
            DatabaseConnect db = new DatabaseConnect("jdbc:mysql://localhost:3306/bank");
            if (!db.existUsername(user.getText())) {
                loginMessage.setText("Account does not exist!");
                user.setText("");
                pass.setText("");
            } else if (!db.validAccount(user.getText(),pass.getText())) {
                loginMessage.setText("Wrong password!");
                user.setText("");
                pass.setText("");
            }
            else {
                    if (db.validAccount(user.getText(), pass.getText())) {
                        currentUser = user.getText();
                        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("loggedMenu.fxml"));
                        Stage window = (Stage) loginButton.getScene().getWindow();
                        window.setScene(new Scene(fxmlLoader.load(), 800, 600));
                    } else {
                        loginMessage.setText("Wrong username or password / Account does not exist!");
                        user.setText("");
                        pass.setText("");
                    }
            }
        }
    }

    public void registerButtonHandle() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("register.fxml"));
        Stage window = (Stage) registerButton.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load(),800, 600));
    }
}