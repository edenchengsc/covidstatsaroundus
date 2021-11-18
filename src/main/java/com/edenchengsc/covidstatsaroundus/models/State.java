package com.edenchengsc.covidstatsaroundus.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class State {

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(String lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Metrics getMetrics() {
        return metrics;
    }

    public void setMetrics(Metrics metrics) {
        this.metrics = metrics;
    }

    //private String country;
    //private String county;
    //private int population;
    private String state;
    private String lastUpdatedDate;
    private String url;
    private Metrics metrics;

    public RiskLevels getRiskLevels() {
        return riskLevels;
    }

    public void setRiskLevels(RiskLevels riskLevels) {
        this.riskLevels = riskLevels;
    }

    private RiskLevels riskLevels;

    @Override
    public String toString() {
        return "Value{" +
                "state=" + state +
                ", url='" + url + '\'' +
                '}';
    }
}
