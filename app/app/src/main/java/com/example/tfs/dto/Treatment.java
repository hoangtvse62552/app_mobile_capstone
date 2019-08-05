package com.example.tfs.dto;

import java.util.List;

public class Treatment {

    private String TreatmentDate;
    private List<String> TreatmentProcess;

    public String getTreatmentDate() {
        return TreatmentDate;
    }

    public void setTreatmentDate(String treatmentDate) {
        TreatmentDate = treatmentDate;
    }

    public List<String> getTreatmentProcess() {
        return TreatmentProcess;
    }

    public void setTreatmentProcess(List<String> treatmentProcess) {
        TreatmentProcess = treatmentProcess;
    }
}
