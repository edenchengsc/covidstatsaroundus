package com.edenchengsc.covidstatsaroundus.models;

public class RiskMap {
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    private String code;
    private int value;
}
