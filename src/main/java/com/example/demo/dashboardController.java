package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class dashboardController {

    @FXML
    private Button account;
    @FXML
    private BorderPane dashBp;

    @FXML
    private TextField customer;

    @FXML
    private Button dashboard;

    @FXML
    private Button employee;

    @FXML
    private Button pOrder;

    @FXML
    private Button sales;

    @FXML
    private Button stocks;

    @FXML
    private Button supplier;

    @FXML
    private ComboBox<?> unit;

    @FXML
    void employeeAction(ActionEvent event) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource("employee.fxml"));
            dashBp.setCenter(view);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    @FXML
    void accountAction(ActionEvent event) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource("account.fxml"));
            dashBp.setCenter(view);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void pOrderAction(ActionEvent event) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource("purchaseOrder.fxml"));
            dashBp.setCenter(view);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void salesAction(ActionEvent event) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource("sales.fxml"));
            dashBp.setCenter(view);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void stocksAction(ActionEvent event) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource("stocks.fxml"));
            dashBp.setCenter(view);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void supplierAction(ActionEvent event) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource("supplier.fxml"));
            dashBp.setCenter(view);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
