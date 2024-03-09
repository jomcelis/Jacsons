package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import java.io.IOException;

public class logInController {

    @FXML
    private Button logIN;

    @FXML
    private TextField uName;

    @FXML
    private PasswordField uPass;

    @FXML
    private Label valid;

    @FXML
    void logInButtonAction(ActionEvent event) {
        String username = uName.getText();
        String password = uPass.getText();

        if (!username.isBlank() && !password.isBlank()) {
            if (username.equals("admin") && password.equals("admin")) {
                valid.setText("Admin Log In Successful!");

                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
                    Parent root = fxmlLoader.load();

                    Scene scene = new Scene(root);

                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                uName.setText("");
                uPass.setText("");
                valid.setText("Invalid Log In Details!");
            }
        } else {
            valid.setText("Invalid Log In Details!");
        }
    }



}
