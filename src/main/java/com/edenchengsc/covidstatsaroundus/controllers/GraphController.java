package com.edenchengsc.covidstatsaroundus.controllers;

import com.edenchengsc.covidstatsaroundus.models.County;
import com.edenchengsc.covidstatsaroundus.models.State;
import com.edenchengsc.covidstatsaroundus.service.CovidDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

@Controller
public class GraphController {

    @Autowired
    CovidDataService covidDataService;

    @GetMapping("/barChart")
    public String barChart(Model model){

        List<County> allCountyStats = covidDataService.getCounties();

        Collections.sort(allCountyStats, new Comparator<County>() {
            @Override
            public int compare(County o1, County o2) {
                return Math.round((o2.getMetrics().getVaccinationsCompletedRatio() - o1.getMetrics().getVaccinationsCompletedRatio()) * 100);
            }
        });

        Map<String, Integer> data = new LinkedHashMap<>();

        for(County county : allCountyStats){
            data.put(county.getCounty().replace(" County", ""), Math.round(county.getMetrics().getVaccinationsCompletedRatio()*100));
        }

        model.addAttribute("keySet", data.keySet());
        model.addAttribute("values", data.values());
        model.addAttribute ("lastUpdatedDate",allCountyStats.get(0).getLastUpdatedDate());
        return "barChart";
    }

    @GetMapping("/vaccineStateBarChart")
    public String vaccineStateBarChart(Model model){

        List<State> allCountyStats = covidDataService.getStateStats();

        Collections.sort(allCountyStats, new Comparator<State>() {
            @Override
            public int compare(State o1, State o2) {
                return Math.round((o2.getMetrics().getVaccinationsCompletedRatio() - o1.getMetrics().getVaccinationsCompletedRatio()) * 100);
            }
        });

        Map<String, Integer> data = new LinkedHashMap<>();

        for(State state : allCountyStats){
            data.put(state.getState(), Math.round(state.getMetrics().getVaccinationsCompletedRatio()*100));
        }

        model.addAttribute("keySet", data.keySet());
        model.addAttribute("values", data.values());

        return "vaccineStateBarChart";
    }

    @GetMapping("/pieChart")
    public String pieChart(Model model){
        model.addAttribute("pass", 90);
        model.addAttribute("fail", 10);
        return "pieChart";
    }


}