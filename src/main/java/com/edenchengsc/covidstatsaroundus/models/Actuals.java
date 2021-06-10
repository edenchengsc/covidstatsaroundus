package com.edenchengsc.covidstatsaroundus.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Actuals {
    public int getCases() {
        return cases;
    }

    public void setCases(int cases) {
        this.cases = cases;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getNewCases() {
        return newCases;
    }

    public void setNewCases(int newCases) {
        this.newCases = newCases;
    }

    public int getNewDeaths() {
        return newDeaths;
    }

    public void setNewDeaths(int newDeaths) {
        this.newDeaths = newDeaths;
    }

    public int getVaccinationsInitiated() {
        return vaccinationsInitiated;
    }

    public void setVaccinationsInitiated(int vaccinationsInitiated) {
        this.vaccinationsInitiated = vaccinationsInitiated;
    }

    public int getGetVaccinationsCompleted() {
        return getVaccinationsCompleted;
    }

    public void setGetVaccinationsCompleted(int getVaccinationsCompleted) {
        this.getVaccinationsCompleted = getVaccinationsCompleted;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String Date) {
        this.date = Date;
    }

    private int cases;
    private int deaths;
    private int newCases;
    private int newDeaths;
    private int vaccinationsInitiated;
    private int getVaccinationsCompleted;
    public String date;

}
