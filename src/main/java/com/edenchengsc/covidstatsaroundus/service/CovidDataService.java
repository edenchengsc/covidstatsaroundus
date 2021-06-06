package com.edenchengsc.covidstatsaroundus.service;

import com.edenchengsc.covidstatsaroundus.CovidStatsAroundUsApplication;
import com.edenchengsc.covidstatsaroundus.models.County;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class CovidDataService {

    private static final Logger log = LoggerFactory.getLogger(CovidStatsAroundUsApplication.class);

    private List<County> allCountyStats = new ArrayList<>();

    public List<County> getCounties(){
        return allCountyStats;
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
        return args -> {
            County[] countyStats = restTemplate.getForObject(
                    "https://api.covidactnow.org/v2/county/WA.json?apiKey=e2592af2a51a42f6acedbe547a95e0da", County[].class);
            log.info(countyStats.toString());

            allCountyStats = Arrays.asList(countyStats.clone());


        };
    }
}
