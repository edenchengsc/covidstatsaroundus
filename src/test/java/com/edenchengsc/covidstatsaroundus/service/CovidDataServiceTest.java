package com.edenchengsc.covidstatsaroundus.service;

import com.edenchengsc.covidstatsaroundus.models.County;
import com.edenchengsc.covidstatsaroundus.models.State;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;


class CovidDataServiceTest {

    @Autowired
    CovidDataService covidDataService;


//    @Test
//    void getCounties() {
//        List<County> counties = covidDataService.getCounties();
//        assertThat(counties.size()).isNotZero();
//    }
//
//    @Test
//    void getStateStats() {
//        List<State> states = covidDataService.getStateStats();
//        assertThat(states.size()).isNotZero();
//    }
}