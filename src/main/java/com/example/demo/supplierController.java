package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.swing.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;
import java.util.ResourceBundle;

import static com.example.demo.database.getUnitData;
import static com.example.demo.database.getUnits;

public class supplierController {
    @FXML
    private Button account;

    @FXML
    private TableView<tbl_unit> unit;

    @FXML
    private TableColumn<tbl_unit, String> col_unitColor;

    @FXML
    private TableColumn<tbl_unit, String> col_unitFinish;

    @FXML
    public TableColumn<tbl_unit, Integer> col_unitID;

    @FXML
    private TableColumn<tbl_unit, String> col_unitName;

    @FXML
    private TableColumn<tbl_unit, Integer> col_unitPrice;

    @FXML
    private TableColumn<tbl_unit, String> col_unitType;

    @FXML
    private Button dashboard;

    @FXML
    private Button employee;

//    @FXML
//    private TextArea input_color;


    @FXML
    private TextArea input_price;

    @FXML
    private TextArea input_name;

    //    @FXML
//    private TextArea input_type;
    @FXML
    private ComboBox<tbl_unit> input_type;

    @FXML
    private Button pOrder;

    @FXML
    private Button sales;

    @FXML
    private Button stocks;

    @FXML
    private Button supplier;

    @FXML
    private Button refreshButton;

    @FXML
    private Button addUnitButton;
    @FXML
    private Button removeUnitButton;
    @FXML
    private Button updateUnitButton;

    @FXML
    private ComboBox<tbl_unit> input_color;

    @FXML
    private ComboBox<tbl_unit> input_finish;




    //    @FXML
//    private ComboBox<tbl_unit> input_name; // Replace the TextArea with ComboBox
    ObservableList<tbl_unit> listM;

    @FXML
    private void removeUnitButton(ActionEvent actionEvent) {
        tbl_unit selectedUnit = unit.getSelectionModel().getSelectedItem();
        if (selectedUnit != null) {
            try {
                removeUnit(selectedUnit);
                refreshTable();
            } catch (Exception e) {
                System.err.println("Error removing unit: " + e.getMessage());
                // Consider showing an error message to the user
            }
        } else {
            // Handle the case where no row is selected (optional, e.g., show a message)
        }
    }

    @FXML
    public void initialize() throws Exception {
        //TableColumn<tbl_unit, Integer> idColumn = new TableColumn<>("ggaga");
        //TableColumn<tbl_unit, String> nameColumn = new TableColumn<>("Name");
        //TableColumn<tbl_unit, Integer> ageColumn = new TableColumn<>("Age");
        col_unitID.setCellValueFactory(new PropertyValueFactory<>("unitCode"));
        col_unitName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        col_unitType.setCellValueFactory(new PropertyValueFactory<>("Type"));
        col_unitColor.setCellValueFactory(new PropertyValueFactory<>("Color"));
        col_unitFinish.setCellValueFactory(new PropertyValueFactory<>("Finish"));
        col_unitPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));

        ObservableList<tbl_unit> unitTypes = getUnitData("unit_type", "unitType", "unitTypeName");
        input_type.setItems(unitTypes);

        ObservableList<tbl_unit> unitColors = getUnitData("unit_color", "unitColorID", "unitColorName");
        input_color.setItems(unitColors);

        ObservableList<tbl_unit> unitFinishes = getUnitData("unit_finish", "unitFinishID", "unitFinishName");
        input_finish.setItems(unitFinishes);

        addUnitButton.setOnAction(event -> {
            String name = input_name.getText();
            String unitTypeName = String.valueOf(input_type.getValue()); // Assuming the ComboBox returns the unitTypeName
            String unitColorName = String.valueOf(input_color.getValue());
            String unitFinishName = String.valueOf(input_finish.getValue());
            String priceStr = input_price.getText(); // Get the price string

            // Validate price string
            int price;
            try {
                price = Integer.parseInt(priceStr);

                // ... rest of the code to create and insert the unit ...

            } catch (NumberFormatException e) {
                // Handle invalid price format:
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Price");
                alert.setHeaderText("Please enter a valid integer for price.");
                alert.setContentText("The entered price is not a valid integer.");
                alert.showAndWait();
                return; // Prevent further processing
            }

            // Call the conversion functions (assuming successful price conversion)
            String type = null;
            String color = null;
            String finish = null;
            try {
                type = convertUnitTypeToStringId(unitTypeName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                color = convertUnitColorToStringId(unitColorName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                finish = convertUnitFinishToStringId(unitFinishName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            tbl_unit newUnit = new tbl_unit(0, name, type, color, finish, price); // Use 0 for auto-increment ID

            try {
                if(checkForDuplicateUnit(newUnit)){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Duplicate Unit");
                    alert.setHeaderText("A unit with the same type, color, and finish already exists.");
                    alert.setContentText("Please modify the unit details to avoid duplicates.");
                    alert.showAndWait();
                    return;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }


            // Validate data if you added the isValidUnitData() method
            if (newUnit.isValidUnitData()) {
                try {
                    insertUnit(newUnit); // Call method to insert data
                    refreshTable(); // Update table
                } catch (Exception e) {
                    // Handle database exception (show error message)
                    System.err.println("Error adding unit: " + e.getMessage());
                }
            } else {
                // Show error message for invalid data (optional)
            }
        });

        updateUnitButton.setOnAction(event -> {
            // Get selected unit from table (assuming a unit is selected)
            tbl_unit selectedUnit = unit.getSelectionModel().getSelectedItem();
            if (selectedUnit == null) {
                // Handle no selection case (e.g., show error message)
                System.err.println("No unit selected for update.");
                return;
            }

            String name = input_name.getText();
            String unitTypeName = String.valueOf(input_type.getValue()); // Assuming the ComboBox returns the unitTypeName
            String unitColorName = String.valueOf(input_color.getValue());
            String unitFinishName = String.valueOf(input_finish.getValue());
            String priceStr = input_price.getText(); // Get the price string

            try {
                int price = Integer.parseInt(priceStr);

                // ... rest of the code to validate other fields (if needed) ...

                // Convert unit type, color, and finish names to IDs (if applicable)
                String type = null;
                String color = null;
                String finish = null;
                try {
                    type = convertUnitTypeToStringId(unitTypeName);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                try {
                    color = convertUnitColorToStringId(unitColorName);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                try {
                    finish = convertUnitFinishToStringId(unitFinishName);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                // Update the selected unit object with new values
                selectedUnit.setName(name);
                selectedUnit.setType(type);
                selectedUnit.setColor(color);
                selectedUnit.setFinish(finish);
                selectedUnit.setPrice(price);

                // Call method to update data in database
                try {
                    updateUnit(selectedUnit);
                    refreshTable(); // Update table
                } catch (Exception e) {
                    // Handle database exception (show error message)
                    System.err.println("Error updating unit: " + e.getMessage());
                }

            } catch (NumberFormatException e) {
                // Handle invalid price format:
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Price");
                alert.setHeaderText("Please enter a valid integer for price.");
                alert.setContentText("The entered price is not a valid integer.");
                alert.showAndWait();
            }
        });



        removeUnitButton.setOnAction(actionEvent -> {
            tbl_unit selectedUnit = unit.getSelectionModel().getSelectedItem();
            if (selectedUnit != null) {
                try {
                    removeUnit(selectedUnit);
                    refreshTable();
                } catch (Exception e) {
                    System.err.println("Error removing unit: " + e.getMessage());
                    // Consider showing an error message to the user
                }
            } else {
                // Handle the case where no row is selected (optional, e.g., show a message)
            }
        });

        unit.getColumns().addAll(col_unitID,col_unitName,col_unitType,col_unitColor,col_unitFinish,col_unitPrice);
        unit.setItems(getUnits());
        refreshButton.setOnAction(event -> {
            try {
                refreshTable();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }


    //Buttons
    private void refreshTable() throws Exception {
        listM = getUnits(); // Fetch updated data
        unit.setItems(listM); // Update table items
    }

    private void insertUnit(tbl_unit unit) throws Exception {
        Connection connection = database.getConnection();

        String query = "INSERT INTO `unit` (`unitCode`, `unitName`, `unitPrice`, `unitType`, `unitColor`, `unitFinish`) " +
                "VALUES (NULL, '" + unit.getName() + "', '" + unit.getPrice() + "', '" + unit.getType() + "', '" + unit.getColor() + "', '" + unit.getFinish() + "')";        System.out.println(query);
        PreparedStatement statement = connection.prepareStatement(query);
        System.out.println(unit.getName());
        statement.executeUpdate();

        database.closeConnection(connection);
    }

    private void removeUnit(tbl_unit unit) throws Exception {
        Connection connection = database.getConnection();

        String query = "DELETE FROM `unit` WHERE unitCode = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, unit.getUnitCode());
        statement.executeUpdate();
        database.closeConnection(connection);
    }

    private void updateUnit(tbl_unit unit) throws Exception {
        Connection connection = database.getConnection();

        // Build update query using prepared statement for safety
        String query = "UPDATE `unit` SET `unitName` = '" + unit.getName() + "', `unitPrice` = '" + unit.getPrice() + "', `unitType` = '" + unit.getType() + "', `unitColor` = '" + unit.getColor() + "', `unitFinish` = '" + unit.getFinish() + "' WHERE `unitCode` = '" + unit.getUnitCode() + "'";
        PreparedStatement statement = connection.prepareStatement(query);
//        statement.setString(1, unit.getName());
//        statement.setInt(2, unit.getPrice());
//        statement.setString(3, unit.getType());
//        statement.setString(4, unit.getColor());
//        statement.setString(5, unit.getFinish());
//        statement.setInt(6, unit.getUnitCode()); // Use existing unit code for update

        statement.executeUpdate();

        database.closeConnection(connection);
    }

    //    Getters
    private ObservableList<tbl_unit> getUnitTypes() throws Exception {
        Connection connection = database.getConnection();
        ObservableList<tbl_unit> types = FXCollections.observableArrayList();

        try {
            String query = "SELECT unitType, unitTypeName FROM unit_type"; // Retrieve both ID and name
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                int unitType = rs.getInt("unitType"); // Extract the ID
                String unitTypeName = rs.getString("unitTypeName");
                types.add(new tbl_unit(unitType, null, unitTypeName, null, null, 0 )); // Add the ID to the tbl_unit object
            }
        } finally {
            database.closeConnection(connection);
        }
        return types;
    }

    private ObservableList<tbl_unit> getUnitColor() throws Exception{
        Connection connection = database.getConnection();
        ObservableList<tbl_unit> types = FXCollections.observableArrayList();

        try{
            String query = "SELECT unitColorID, unitColorName FROM unit_color";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

            while(rs.next()){
                int unitColor = rs.getInt("unitColorID");
                String unitColorName = rs.getString("unitColorName");
                types.add(new tbl_unit(0, null, null, unitColorName, null, 0 )); // Add the ID to the tbl_unit object
            }
        }finally {
            database.closeConnection(connection);
        }
        return types;
    }

    // Converters
    private String convertUnitTypeToStringId(String unitTypeName) throws Exception {
        Connection connection = database.getConnection();
        try {
            String query = "SELECT unitType FROM unit_type WHERE unitTypeName = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, unitTypeName);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return rs.getString("unitType");
            } else {
                return unitTypeName; // Return original name if not found
            }
        } finally {
            database.closeConnection(connection);
        }
    }

    private String convertUnitFinishToStringId(String unitFinishName) throws Exception {
        Connection connection = database.getConnection();
        try {
            String query = "SELECT unitFinishID FROM unit_finish WHERE unitFinishName = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, unitFinishName);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return rs.getString("unitFinishID");
            } else {
                return unitFinishName; // Return original name if not found
            }
        } finally {
            database.closeConnection(connection);
        }
    }

    private String convertUnitColorToStringId(String unitColorName) throws Exception {
        Connection connection = database.getConnection();
        try {
            String query = "SELECT unitColorID FROM unit_color WHERE unitColorName = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, unitColorName);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return rs.getString("unitColorID");
            } else {
                return unitColorName; // Return original name if not found
            }
        } finally {
            database.closeConnection(connection);
        }
    }

    private boolean checkForDuplicateUnit(tbl_unit unit) throws Exception {
        Connection connection = database.getConnection();
        try {
            String query = "SELECT * FROM `unit` WHERE `unitType` = '" + unit.getType() + "' AND `unitColor` = '" + unit.getColor() + "' AND `unitFinish` = '" + unit.getFinish() + "'";
            PreparedStatement statement = connection.prepareStatement(query);

            ResultSet rs = statement.executeQuery();

            return rs.next(); // Return true if a duplicate is found
        } finally {
            database.closeConnection(connection);
        }
    }

//    public boolean isValidPrice(){
//        int price = Integer.parseInt(input_price.getText());
//        if (price !=)
//    }




}

