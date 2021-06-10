package com.edenchengsc.covidstatsaroundus.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class County {
    private String state;
    private String county;
    private String country;
    private int population;
    private Metrics metrics;

    public Metrics[] getMetricsTimeseries() {
        return metricsTimeseries;
    }

    public void setMetricsTimeseries(Metrics[] metricsTimeseries) {
        this.metricsTimeseries = metricsTimeseries;
    }

    private Metrics[] metricsTimeseries;
    private Actuals actuals;

    public Actuals[] getActualsTimeseries() {
        return actualsTimeseries;
    }

    public void setActualsTimeseries(Actuals[] actualsTimeseries) {
        this.actualsTimeseries = actualsTimeseries;
    }

    private Actuals[] actualsTimeseries;
    private String lastUpdatedDate;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }


    public Metrics getMetrics() {
        return metrics;
    }

    public void setMetrics(Metrics metrics) {
        this.metrics = metrics;
    }

    public Actuals getActuals() {
        return actuals;
    }

    public void setActuals(Actuals actuals) {
        this.actuals = actuals;
    }

    public String getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(String lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    @Override
    public String toString() {
        return "Value{" +
                "state=" + state +
                ", population='" + population + '\'' +
                '}';
    }

}
