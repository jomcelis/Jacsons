package com.example.demo;

    // Constructor
    public class tbl_inventory {
        private int inventoryId;
        private int unitCode;
        private int qty;
        private String unitName; // Add this property

        // Constructor
        public tbl_inventory(int inventoryId, int unitCode, int qty, String unitName) {
            this.inventoryId = inventoryId;
            this.unitCode = unitCode;
            this.qty = qty;
            this.unitName = unitName; // Set unitName in constructor
        }

    // Getters
    public int getInventoryId() {
        return inventoryId;
    }

    public int getUnitCode() {
        return unitCode;
    }

    public int getQty() {
        return qty;
    }

    // Setters
    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public void setUnitCode(int unitCode) {
        this.unitCode = unitCode;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

        public String getUnitName() {
            return unitName;
        }

        public void setUnitName(String unitName) {
            this.unitName = unitName;
        }
}
