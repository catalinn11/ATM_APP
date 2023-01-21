package com.example.atm_online;

import javafx.event.ActionEvent;
import javafx.event.Event;
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
import java.security.Key;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class TransferController extends LoggedMenuController implements Initializable {
    @FXML
    private Button TconfirmButton;
    @FXML
    private Button backButton;
    @FXML
    private Label transferMessage;
    @FXML
    private TextField transferAmount;
    @FXML
    private TextField transferUser;
    @FXML
    private ImageView minImage;
    @FXML
    private ImageView expImage;
    @FXML
    private ImageView closeImage;
    @FXML
    private Pane titleBar;

    String regex = "[0-9]+";

    private double xOffset = 0, yOffset = 0;

    EventHandler<KeyEvent> enterPress = new EventHandler<>() {
        @Override
        public void handle(KeyEvent keyEvent) {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                try {
                    TconfirmButtonHandle();
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

        transferAmount.setOnKeyPressed(enterPress);
    }
    public void TconfirmButtonHandle() throws SQLException {
        if (transferUser.getText().isEmpty() && transferAmount.getText().isEmpty()) {
            transferMessage.setText("Please complete all fields!");
        } else if (transferAmount.getText().isEmpty()) {
            transferMessage.setText("Please enter an amount!");
        } else if (transferUser.getText().isEmpty()) {
            transferMessage.setText("Please enter the user!");
        } else if (!transferAmount.getText().matches(regex) || Double.parseDouble(transferAmount.getText()) <=0) {
            transferMessage.setText("Please enter a valid amount!");
        } else {
            DatabaseConnect db = new DatabaseConnect("jdbc:mysql://localhost:3306/bank");
            boolean amountEnough = Double.parseDouble(transferAmount.getText()) <= db.getBalance(currentUser);
            boolean existTransferUser = db.existUsername(transferUser.getText());
            if (!existTransferUser || transferUser.getText().equals(currentUser)) {
                transferMessage.setText("User account does not exist!");
            }
            else if (!amountEnough) {
                transferMessage.setText("You do not have enough money to transfer!");
            }
            else {
                db.setBalance(currentUser, db.getBalance(currentUser) - Double.parseDouble(transferAmount.getText()));
                db.setBalance(transferUser.getText(), db.getBalance(transferUser.getText()) + Double.parseDouble(transferAmount.getText()));
                transferMessage.setText("You have successfully transfered " + transferAmount.getText() + " RON"
                                        + " to \n" + transferUser.getText() + " (" + db.getFirstname(transferUser.getText())
                                        + " " + db.getLastname(transferUser.getText()) + ")");
            }
        }
    }

    public void backButtonHandle() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("loggedMenu.fxml"));
        Stage window = (Stage) backButton.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load(),800, 600));
    }

}
