package com.edenchengsc.covidstatsaroundus.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SingleCountySummary {

    public County[] getCountyStats() {
        return countyStats;
    }

    public void setCountyStats(County[] countyStats) {
        this.countyStats = countyStats;
    }

    private County[] countyStats;

}

