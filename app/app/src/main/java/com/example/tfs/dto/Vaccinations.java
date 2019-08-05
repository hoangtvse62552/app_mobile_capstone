package com.example.tfs.dto;

public class Vaccinations {
    private String VaccinationDate;
    private String VaccinationType;

    public Vaccinations() {
    }

    public Vaccinations(String vaccinationDate, String vaccinationType) {
        VaccinationDate = vaccinationDate;
        VaccinationType = vaccinationType;
    }

    public String getVaccinationDate() {
        return VaccinationDate;
    }

    public void setVaccinationDate(String vaccinationDate) {
        VaccinationDate = vaccinationDate;
    }

    public String getVaccinationType() {
        return VaccinationType;
    }

    public void setVaccinationType(String vaccinationType) {
        VaccinationType = vaccinationType;
    }
}
