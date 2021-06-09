package com.edenchengsc.covidstatsaroundus.controllers;

import com.edenchengsc.covidstatsaroundus.models.County;
import com.edenchengsc.covidstatsaroundus.service.CovidDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public class HomeController {

    @Autowired
    CovidDataService covidDataService;

    @GetMapping("/")
    public String home(Model model){
        List<County> counties = covidDataService.getCounties();

        //County king = counties.stream().filter(county ->"King County".equals(county.getCounty())).findAny().orElse(null);
        model.addAttribute ("lastUpdatedDate",counties.get(0).getLastUpdatedDate());
//        model.addAttribute ("totalCases", king.getActuals().getCases());
//        model.addAttribute ("totalDeaths", king.getActuals().getDeaths());
//        model.addAttribute ("newCases", king.getActuals().getNewCases());
//        model.addAttribute ("newDeath", king.getActuals().getNewDeaths());
//        model.addAttribute ("testPositivityRatio", king.getMetrics().getTestPositivityRatio());
//        model.addAttribute ("caseDensity", king.getMetrics().getCaseDensity());
//        model.addAttribute ("infectionRate",king.getMetrics().getInfectionRate());
//        model.addAttribute ("vaccinationsInitiatedRatio", king.getMetrics().getVaccinationsInitiatedRatio());
//        model.addAttribute ("vaccinationsCompletedRatio",king.getMetrics().getVaccinationsCompletedRatio());
//        model.addAttribute ("counties",counties);
//        model.addAttribute ("lastUpdatedDate",king.getLastUpdatedDate());

        return "index";
    }
}
