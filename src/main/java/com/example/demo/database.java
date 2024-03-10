package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class database {

    public static Connection getConnection() throws Exception {
        // Replace with your database connection details
        String url = "jdbc:mariadb://localhost:3306/db_jacsons";
        String username = "root";
        String password = "";
        return DriverManager.getConnection(url, username, password);
    }

    public static void closeConnection(Connection connection) throws Exception {
        if (connection != null) {
            connection.close();
        }
    }

    public static ObservableList<tbl_unit> getUnits() throws Exception {
        Connection connection = database.getConnection();
        ObservableList<tbl_unit> list = FXCollections.observableArrayList();

        try {
            String query = "SELECT u.unitCode, u.unitName, ut.unitTypeName, uc.unitColorName, uf.unitFinishName, u.unitPrice " +
                    "FROM unit u " +
                    "JOIN unit_type ut ON u.unitType = ut.unitType " +
                    "JOIN unit_color uc ON u.unitColor = uc.unitColorID " +
                    "JOIN unit_finish uf ON u.unitFinish = uf.unitFinishID;";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                list.add(new tbl_unit(rs.getInt("unitCode"),
                        rs.getString("unitName"),
                        rs.getString("unitTypeName"),
                        rs.getString("unitColorName"),
                        rs.getString("unitFinishName"),
                        rs.getInt("unitPrice")));
            }
        } finally {
            database.closeConnection(connection);
        }
        return list;
    }

    public static ObservableList<tbl_inventory> getInventoryDetails() throws Exception {
        Connection connection = database.getConnection();
        ObservableList<tbl_inventory> inventoryList = FXCollections.observableArrayList();

        try {
            Statement statement = connection.createStatement();
            String query = "SELECT i.inventoryID, i.unitCode, i.currentStock, u.unitName " +
                    "FROM inventory i " +
                    "JOIN unit u ON i.unitCode = u.unitCode;";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                tbl_inventory item = new tbl_inventory(
                        resultSet.getInt("inventoryID"),
                        resultSet.getInt("unitCode"),
                        resultSet.getInt("currentStock"),
                        resultSet.getString("u.unitName")  // Added unitName
                );
                inventoryList.add(item);
            }
        } finally {
            database.closeConnection(connection);
        }

        return inventoryList;
    }


    public static ObservableList<tbl_unit> getUnitData(String tableName, String idColumn, String nameColumn) throws Exception {
        Connection connection = database.getConnection();
        ObservableList<tbl_unit> units = FXCollections.observableArrayList();

        try {
            String query = "SELECT " + idColumn + ", " + nameColumn + " FROM " + tableName;
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt(idColumn);
                String name = rs.getString(nameColumn);
                units.add(new tbl_unit(id, null, name, null, null, 0));
            }
        } finally {
            database.closeConnection(connection);
        }
        return units;
    }



}
