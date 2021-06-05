package com.edenchengsc.covidstatsaroundus.DomainClass;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SingleCountySummary {

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

    private String lastUpdatedDate;
    private String url;

    public County[] getCountyStats() {
        return countyStats;
    }

    public void setCountyStats(County[] countyStats) {
        this.countyStats = countyStats;
    }

    private County[] countyStats;

    @Override
    public String toString() {
        return "lastUpdatedDate: " + lastUpdatedDate
                + " URL:" + url;
    }
}

