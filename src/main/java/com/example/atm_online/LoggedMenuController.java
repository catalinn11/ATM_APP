package com.example.atm_online;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class LoggedMenuController extends HelloController implements Initializable {
    @FXML
    private ImageView minImage;
    @FXML
    private ImageView expImage;
    @FXML
    private ImageView closeImage;
    @FXML
    private Pane titleBar;
    @FXML
    private Label welcomeMessage;
    @FXML
    private ImageView genderIcon;
    @FXML
    private Label balance;
    @FXML
    private Button depositButton;
    @FXML
    private Button withdrawButton;
    @FXML
    private Button logoutButton;
    @FXML
    private Button transferButton;

    private double xOffset = 0, yOffset = 0;

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

        try {
            DatabaseConnect db = new DatabaseConnect("jdbc:mysql://localhost:3306/bank");
            welcomeMessage.setText("Welcome " + db.getFirstname(currentUser).toUpperCase() + " " + db.getLastname(currentUser).toUpperCase());
            balance.setText(db.getBalance(currentUser) + " RON");
            if (db.getGender(currentUser).equals("M")) {
                genderIcon.setImage(new Image("male.png"));
            }
            else {
                genderIcon.setImage(new Image("female.png"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void depositButtonHandle(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("deposit.fxml"));
        Stage window = (Stage) depositButton.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load(),800, 600));
    }

    public void withdrawButtonHandle(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("withdraw.fxml"));
        Stage window = (Stage) withdrawButton.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load(),800, 600));
    }
    public void transferButtonHandle(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("transfer.fxml"));
        Stage window = (Stage) transferButton.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load(),800, 600));
    }
    public void logoutButtonHandle(ActionEvent e) throws IOException {
        currentUser = "";
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Stage window = (Stage) logoutButton.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load(),800, 600));
    }

}
