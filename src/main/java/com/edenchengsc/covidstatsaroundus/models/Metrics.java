package com.edenchengsc.covidstatsaroundus.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Metrics {
    private float vaccinationsInitiatedRatio;

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

    private float vaccinationsCompletedRatio;


}
