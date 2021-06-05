package com.edenchengsc.covidstatsaroundus;

import com.edenchengsc.covidstatsaroundus.DomainClass.County;
import com.edenchengsc.covidstatsaroundus.DomainClass.SingleCountySummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@SpringBootApplication
public class CovidStatsAroundUsApplication {

	private static final Logger log = LoggerFactory.getLogger(CovidStatsAroundUsApplication.class);
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

			Map<String, Float> map = new HashMap<>();
			for(County county : countyStats){
				map.put(county.getCounty(), county.getMetrics().getVaccinationsCompletedRatio());
				log.info(county.getCounty() + " " + county.getMetrics().getVaccinationsCompletedRatio());
			}
 
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(CovidStatsAroundUsApplication.class, args);
	}

}
