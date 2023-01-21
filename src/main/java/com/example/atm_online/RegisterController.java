package com.example.atm_online;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class RegisterController implements Initializable {
    @FXML
    private ImageView minImage;
    @FXML
    private ImageView expImage;
    @FXML
    private ImageView closeImage;
    @FXML
    private Pane titleBar;
    @FXML
    private TextField firstname;
    @FXML
    private TextField lastname;
    @FXML
    private TextField user;
    @FXML
    private PasswordField pass;
    @FXML
    private Label registerMessage;
    @FXML
    private Button createAccount;
    @FXML
    private ChoiceBox genderChoice;
    @FXML
    private ImageView registerUser;
    @FXML
    private ImageView bankicon;
    @FXML
    private Button backButton;

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

        genderChoice.getItems().add("M");
        genderChoice.getItems().add("F");
        registerUser.setImage(new Image("add-user.png"));
        bankicon.setImage(new Image("icon.png"));
    }


    public void createAccountButtonHandle(ActionEvent e) throws SQLException, IOException {
        DatabaseConnect db = new DatabaseConnect("jdbc:mysql://localhost:3306/bank");
        boolean empty = user.getText().isEmpty() || lastname.getText().isEmpty() || genderChoice.getSelectionModel().isEmpty()
                || user.getText().isEmpty() || pass.getText().isEmpty();

        if (empty) {
            registerMessage.setText("Please complete all required fields!");
        }
        else if (db.existUsername(user.getText())) {
            registerMessage.setText("Username already taken!");
        }
        else {
            db.insert(firstname.getText(),lastname.getText(), genderChoice.getValue().toString(),user.getText(),pass.getText());
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
            Stage window = (Stage) createAccount.getScene().getWindow();
            window.setScene(new Scene(fxmlLoader.load(),800, 600));
        }
    }

    public void backButtonHandle(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Stage window = (Stage) backButton.getScene().getWindow();
        window.setScene(new Scene(fxmlLoader.load(),800, 600));
    }
}
