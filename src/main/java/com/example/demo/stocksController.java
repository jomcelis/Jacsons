package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;

import static com.example.demo.database.getInventoryDetails;

public class stocksController {

    @FXML
    private TableView<tbl_inventory> inventoryTable;
    @FXML
    private TableColumn<tbl_inventory, Integer> col_InventoryID;
    @FXML
    private TableColumn<tbl_inventory, Integer> col_unitCode;
    @FXML
    private TableColumn<tbl_inventory, Integer> col_Qty;
    @FXML
    private  TableColumn<tbl_inventory,Integer> col_unitName;
    @FXML
    private TextField input_Qty;
    @FXML
    private Button updateQtyButton;


    public void initialize() throws Exception { // Called after FXML is loaded
        // Get inventory data from database

        ObservableList<tbl_inventory> inventoryList = getInventoryDetails();
        inventoryTable.setItems(inventoryList);

        col_InventoryID.setCellValueFactory(new PropertyValueFactory<>("inventoryId"));
        col_unitCode.setCellValueFactory(new PropertyValueFactory<>("unitCode"));
        col_Qty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        inventoryTable.setItems(getInventoryDetails());

        // Set items to the table
        updateQtyButton.setOnAction(actionEvent -> {
            int qty = Integer.parseInt(input_Qty.getText());
            try {
                // Get the selected item from the table
                tbl_inventory selectedItem = inventoryTable.getSelectionModel().getSelectedItem();

                if (selectedItem != null) {
                    int unitCodeToUpdate = selectedItem.getInventoryId(); // Get unitCode from selected item
                    updateQuantity(unitCodeToUpdate, qty); // Use unitCode in updateQuantity
                } else {
                    // Handle no selection case (e.g., display an error message)
                    System.out.println("Please select a row to update!");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

    }

        public void updateQuantity(int inventoryId, int quantityToAdd) throws Exception {
        Connection connection = database.getConnection(); // Assuming database.getConnection() is available
        try {
            String query = "UPDATE inventory_details SET quantity = quantity + ? WHERE inventoryID = ?"; // Using quantity + ? to add
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, quantityToAdd); // Provide the amount to add
            statement.setInt(2, inventoryId);
            statement.executeUpdate();

            // Update table data (recommended)
            ObservableList<tbl_inventory> inventoryList = getInventoryDetails();
            inventoryTable.setItems(inventoryList);
        } finally {
            database.closeConnection(connection);
        }
    }



    @FXML
    public void handleUpdateButtonClick() throws Exception {
        // Get the selected item from the table
        tbl_inventory selectedItem = inventoryTable.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            // Get the new quantity from the input field
            int newQuantity = Integer.parseInt(input_Qty.getText());

            // Update the quantity in the database
            updateQuantity(selectedItem.getInventoryId(), newQuantity);
        } else {
            // Handle no selection case (e.g., display an error message)
            System.out.println("Please select a row to update!");
        }
    }
}
