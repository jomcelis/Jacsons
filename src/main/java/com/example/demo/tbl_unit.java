package com.example.demo;

public class tbl_unit {

    @Override
    public String toString() {
        return getType(); // Assuming "type" holds the unit type name for display
    }

    private String name;
    private String type;
    private String color;
    private String finish;
    private int price;
    private int unitCode;

    // Constructor
    public tbl_unit(int unitCode, String name, String type, String color, String finish, int price) {
        this.unitCode = unitCode;
        this.name = name;
        this.type = type;
        this.color = color;
        this.finish = finish;
        this.price = price;
    }

    // Getters and setters (if needed)

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getUnitCode(){return unitCode;}
    public void setUnitCode(int unitCode){this.unitCode = unitCode;}

    public String getFormattedUnitCode() {
        // Implement custom formatting if needed (e.g., padding with zeros)
        return String.valueOf(unitCode);
    }

    public boolean isValidUnitData() {
        // Implement logic to check if entered data is valid for the database
        return true; // Replace with actual validation
    }


}


