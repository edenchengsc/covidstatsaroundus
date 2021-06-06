package com.edenchengsc.covidstatsaroundus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

@SpringBootApplication
public class CovidStatsAroundUsApplication {

	public void addViewController(ViewControllerRegistry registry){
		registry.addViewController("/index").setViewName("index");
	}

	public static void main(String[] args) {
		SpringApplication.run(CovidStatsAroundUsApplication.class, args);
	}

}
