package com.edenchengsc.covidstatsaroundus.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Metrics {
    private float testPositivityRatio;
    private float caseDensity;
    private float infectionRate;
    private float vaccinationsInitiatedRatio;
    private float vaccinationsCompletedRatio;
    public String lastUpdatedDate;

    public float getTestPositivityRatio() {
        return testPositivityRatio;
    }

    public void setTestPositivityRatio(float testPositivityRatio) {
        this.testPositivityRatio = testPositivityRatio;
    }

    public float getCaseDensity() {
        return caseDensity;
    }

    public void setCaseDensity(float caseDensity) {
        this.caseDensity = caseDensity;
    }

    public float getInfectionRate() {
        return infectionRate;
    }

    public void setInfectionRate(float infectionRate) {
        this.infectionRate = infectionRate;
    }

    public float getVaccinationsInitiatedRatio() {
        return vaccinationsInitiatedRatio;
    }

    public void setVaccinationsInitiatedRatio(float vaccinationsInitiatedRatio) {
        this.vaccinationsInitiatedRatio = vaccinationsInitiatedRatio;
    }

    public float getVaccinationsCompletedRatio() {
        return vaccinationsCompletedRatio;
    }

    public void setVaccinationsCompletedRatio(float vaccinationsCompletedRatio) {
        this.vaccinationsCompletedRatio = vaccinationsCompletedRatio;
    }




}
