package com.edenchengsc.covidstatsaroundus.controllers;

import com.edenchengsc.covidstatsaroundus.models.Actuals;
import com.edenchengsc.covidstatsaroundus.models.County;
import com.edenchengsc.covidstatsaroundus.models.Metrics;
import com.edenchengsc.covidstatsaroundus.service.CovidDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class HomeController {

    @Autowired
    CovidDataService covidDataService;

    @GetMapping("/")
    public String home(Model model){

        //Single County data
        County specifiedCounty = covidDataService.getSpecifiedCounty();
        model.addAttribute ("lastUpdatedDate", specifiedCounty.getLastUpdatedDate());
        model.addAttribute ("testPositivityRatio", specifiedCounty.getMetrics().getTestPositivityRatio());
        model.addAttribute ("infectionRate", specifiedCounty.getMetrics().getInfectionRate());
        model.addAttribute ("vaccinationsInitiatedRatio", specifiedCounty.getMetrics().getVaccinationsInitiatedRatio());
        model.addAttribute ("vaccinationsCompletedRatio", specifiedCounty.getMetrics().getVaccinationsCompletedRatio());


        //line chart new cases/new death
        List<Actuals>  specifiedCountyActualsTimeseries = covidDataService.getSpecifiedCountyActualsTimeseries();

        Map<String, Integer> newCasesMap = new HashMap<>();
        Map<String, Integer> newDeathMap = new HashMap<>();

        for(Actuals actuals : specifiedCountyActualsTimeseries){
                System.out.println(actuals.getDate() + ": " + actuals.getNewCases());
                newCasesMap.put(actuals.getDate(), actuals.getNewCases());
                newDeathMap.put(actuals.getDate(), actuals.getNewDeaths());
        }

        model.addAttribute("Dates", newDeathMap.keySet());
        model.addAttribute("NewCases", newCasesMap.values());
        model.addAttribute("NewDeaths", newDeathMap.values());

//        List<County> allCountyStats = covidDataService.getCounties();
//        Collections.sort(allCountyStats, new Comparator<County>() {
//            @Override
//            public int compare(County o1, County o2) {
//                return Math.round((o2.getMetrics().getVaccinationsCompletedRatio() - o1.getMetrics().getVaccinationsCompletedRatio()) * 100);
//            }
//        });
//        Map<String, Integer> data = new LinkedHashMap<>();
//        for(County county : allCountyStats){
//            data.put(county.getCounty().replace(" County", ""), Math.round(county.getMetrics().getVaccinationsCompletedRatio()*100));
//        }
//        model.addAttribute("keySet", data.keySet());
//        model.addAttribute("values", data.values());
//        //model.addAttribute ("lastUpdatedDate",allCountyStats.get(0).getLastUpdatedDate());


        return "index";
    }

}
