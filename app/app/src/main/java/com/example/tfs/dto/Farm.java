package com.example.tfs.dto;

import java.util.List;

public class Farm {
    private String name;
    private String address;
    private List<String> feedings;
    private List<Vaccinations> vaccinations;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getFeedings() {
        return feedings;
    }

    public void setFeedings(List<String> feedings) {
        this.feedings = feedings;
    }

    public List<Vaccinations> getVaccinations() {
        return vaccinations;
    }

    public void setVaccinations(List<Vaccinations> vaccinations) {
        this.vaccinations = vaccinations;
    }

    public Farm() {
    }
}
