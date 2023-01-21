package com.example.atm_online;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DepositController extends LoggedMenuController implements Initializable {
    @FXML
    private Button DconfirmButton;
    @FXML
    private Button backButton;
    @FXML
    private Label depositMessage;
    @FXML
    private TextField depositAmount;
    @FXML
    private ImageView minImage;
    @FXML
    private ImageView expImage;
    @FXML
    private ImageView closeImage;
    @FXML
    private Pane titleBar;
    private double xOffset = 0, yOffset = 0;
    String regex = "[0-9]+";

    EventHandler<KeyEvent> enterPress = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent keyEvent) {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                try {
                    DconfirmButtonHandle();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
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

        depositAmount.setOnKeyPressed(enterPress);
    }
    public void DconfirmButtonHandle() throws SQLException {
        if (depositAmount.getText().isEmpty()) {
            depositMessage.setText("Please enter an amount!");
        } else if (!depositAmount.getText().matches(regex)) {
            depositMessage.setText("Please enter a valid amount!");
            depositAmount.setText("");
        } else if (Double.parseDouble(depositAmount.getText()) < 0) {
            depositMessage.setText("Please enter a valid amount!");
            depositAmount.setText("");
        } else {
            DatabaseConnect db = new DatabaseConnect("jdbc:mysql://localhost:3306/bank");
            Double newBalance = db.getBalance(currentUser) + Double.parseDouble(depositAmount.getText());
            db.setBalance(currentUser, newBalance);
            depositMessage.setText("You have successfully deposited " + depositAmount.getText() + " RON");
        }
    }

    public void backButtonHandle() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("loggedMenu.fxml"));
        Stage window = (Stage) backButton.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load(),800, 600));
    }

}
