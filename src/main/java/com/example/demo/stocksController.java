package com.example.demo;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;

import static com.example.demo.database.getInventoryDetails;
import static com.example.demo.database.getUnitData;

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
    private TableColumn<tbl_inventory, Integer> col_unitName;
    @FXML
    private TextField input_Qty;
    @FXML
    private Button updateQtyButton;
    @FXML
    private ComboBox<tbl_unit> input_name;
    @FXML
    private Button addToInventoryButton;

    ObservableList<tbl_inventory> listM;


    public void initialize() throws Exception {
        ObservableList<tbl_inventory> inventoryList = getInventoryDetails();
        ObservableList<tbl_unit> unitTypes = getUnitData("unit", "unitCode", "unitName");
        input_name.setItems(unitTypes);

        // Configure columns
        col_InventoryID.setCellValueFactory(new PropertyValueFactory<>("inventoryId"));
        col_unitCode.setCellValueFactory(new PropertyValueFactory<>("unitCode"));
        col_Qty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        col_unitName.setCellValueFactory(new PropertyValueFactory<>("unitName"));

        inventoryTable.setItems(inventoryList);

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

        addToInventoryButton.setOnAction(actionEvent -> {
            String toAdd = String.valueOf(input_name.getValue()); // Access unitName from tbl_unit

            try {
                if (isUnitAlreadyInInventory(toAdd)) { // Check for duplicate unit
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Duplicate Unit");
                    alert.setContentText("The unit '" + toAdd + "' already exists in the inventory!");
                    alert.showAndWait();
                    return; // Prevent further processing if duplicate
                }

                Connection connection = database.getConnection();

                // ... (rest of the code for adding to inventory)
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public boolean isUnitAlreadyInInventory(String unitName) throws Exception {
        Connection connection = database.getConnection(); // Assuming database.getConnection() is available
        String checkQuery = "SELECT * FROM unit WHERE unitName = '" + unitName + "'";
        PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
        ResultSet checkResultSet = checkStatement.executeQuery();
        return checkResultSet.next(); // True if a matching unit is found
    }

        public void updateQuantity(int inventoryId, int quantityToAdd) throws Exception {
        Connection connection = database.getConnection(); // Assuming database.getConnection() is available
        try {
            String query = "UPDATE inventory SET currentStock = currentStock + ? WHERE inventoryID = ?"; // Using quantity + ? to add
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

//for commit
