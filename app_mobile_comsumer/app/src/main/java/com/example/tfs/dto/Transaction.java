package com.example.tfs.dto;

public class Transaction {

    private int id;
    private Foods food;
    private String providerName;
    private String providerAddress;
    private String farmerAddress;
    private String farmerName;

    public String getProviderAddress() {
        return providerAddress;
    }

    public void setProviderAddress(String providerAddress) {
        this.providerAddress = providerAddress;
    }

    public String getFarmerAddress() {
        return farmerAddress;
    }

    public void setFarmerAddress(String farmerAddress) {
        this.farmerAddress = farmerAddress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Foods getFood() {
        return food;
    }

    public void setFood(Foods food) {
        this.food = food;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public Transaction(int id, Foods food, String providerName, String farmerName) {
        this.id = id;
        this.food = food;
        this.providerName = providerName;
        this.farmerName = farmerName;
    }

    public Transaction( ) {
    }
}
