package com.edenchengsc.covidstatsaroundus.controllers;

import com.edenchengsc.covidstatsaroundus.models.Actuals;
import com.edenchengsc.covidstatsaroundus.models.County;
import com.edenchengsc.covidstatsaroundus.models.Metrics;
import com.edenchengsc.covidstatsaroundus.models.State;
import com.edenchengsc.covidstatsaroundus.service.CovidDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    CovidDataService covidDataService;

    @GetMapping("/")
    public String home(Model model){

        //Single County data
        County specifiedCounty = covidDataService.getSpecifiedCounty();
        model.addAttribute ("countyName", specifiedCounty.getCounty());
        model.addAttribute ("lastUpdatedDate", specifiedCounty.getLastUpdatedDate());
        model.addAttribute ("testPositivityRatio",  Math.round(specifiedCounty.getMetrics().getTestPositivityRatio() * 100));
        model.addAttribute ("infectionRate", Math.round(specifiedCounty.getMetrics().getInfectionRate()* 100));
        model.addAttribute ("vaccinationsInitiatedRatio", Math.round(specifiedCounty.getMetrics().getVaccinationsInitiatedRatio()* 100));
        model.addAttribute ("vaccinationsCompletedRatio", Math.round(specifiedCounty.getMetrics().getVaccinationsCompletedRatio()* 100));

        //Comparation table
        List<County> allCountyStats = covidDataService.getCounties();
//        Collections.sort(allCountyStats, new Comparator<County>() {
//            @Override
//            public int compare(County o1, County o2) {
//                return Math.round((o2.getMetrics().getVaccinationsCompletedRatio() - o1.getMetrics().getVaccinationsCompletedRatio()) * 100);
//            }
//        });
        Map<String, Integer> data = new LinkedHashMap<>();
        for(County county : allCountyStats){
            data.put(county.getCounty().replace(" County", ""), Math.round(county.getMetrics().getVaccinationsCompletedRatio()*100));
        }
        model.addAttribute("selectedCountyStats", allCountyStats);


        //line chart new cases/new death
        List<Actuals>  specifiedCountyActualsTimeseries = covidDataService.getSpecifiedCountyActualsTimeseries();
        Collections.sort(specifiedCountyActualsTimeseries, new Comparator<Actuals>() {
            @Override
            public int compare(Actuals o1, Actuals o2) {
                return  Integer.parseInt(o1.getDate().replace("-", "")) - Integer.parseInt(o2.getDate().replace("-", ""));
            }
        });

        Map<String, Integer> newCasesMap = new HashMap<>();
        Map<String, Integer> newDeathMap = new HashMap<>();
        Map<String, Integer> totalCasesMap = new HashMap<>();
        Map<String, Integer> totalDeathMap = new HashMap<>();

        for(Actuals actuals : specifiedCountyActualsTimeseries){
            System.out.println(actuals.getDate() + ": " + actuals.getDeaths());
            newCasesMap.put(actuals.getDate(), actuals.getNewCases());
            newDeathMap.put(actuals.getDate(), actuals.getNewDeaths());
            totalCasesMap.put(actuals.getDate(), actuals.getCases());
            totalDeathMap.put(actuals.getDate(), actuals.getDeaths());
        }

        model.addAttribute("Dates", newDeathMap.keySet());
        model.addAttribute("NewCases", newCasesMap.values());
        model.addAttribute("NewDeaths", newDeathMap.values());
        model.addAttribute("TotalCases", totalCasesMap.values());
        model.addAttribute("TotalDeaths", totalDeathMap.values());


        List<State> allStateStats = covidDataService.getStateStats();

        Collections.sort(allStateStats, new Comparator<State>() {
            @Override
            public int compare(State o1, State o2) {
                return Math.round((o2.getMetrics().getVaccinationsCompletedRatio() - o1.getMetrics().getVaccinationsCompletedRatio()) * 100);
            }
        });

        Map<String, Integer> vaccinationsCompletedRatioData = new LinkedHashMap<>();
        Map<String, Integer> vaccinationsInitiatedRatioData = new LinkedHashMap<>();

        for(State state : allStateStats){
            //System.out.println("State: " + state.getState() + "  - " + Math.round(state.getMetrics().getVaccinationsCompletedRatio()*100) );
            vaccinationsInitiatedRatioData.put(state.getState(), Math.round(state.getMetrics().getVaccinationsInitiatedRatio()*100));
            vaccinationsCompletedRatioData.put(state.getState(), Math.round(state.getMetrics().getVaccinationsCompletedRatio()*100));

        }

        model.addAttribute("keySet", vaccinationsCompletedRatioData.keySet());
        model.addAttribute("vaccinationsInitiatedRatioValues", vaccinationsInitiatedRatioData.values());
        model.addAttribute("vaccinationsCompletedRatioValues", vaccinationsCompletedRatioData.values());



        return "index";
    }

    @GetMapping("/newCaseLineChart")
    public String newCaseLineChart(Model model){

        //Single County data
        County specifiedCounty = covidDataService.getSpecifiedCounty();
        model.addAttribute ("countyName", specifiedCounty.getCounty());
        model.addAttribute ("lastUpdatedDate", specifiedCounty.getLastUpdatedDate());
        model.addAttribute ("testPositivityRatio", specifiedCounty.getMetrics().getTestPositivityRatio());
        model.addAttribute ("infectionRate", specifiedCounty.getMetrics().getInfectionRate());
        model.addAttribute ("vaccinationsInitiatedRatio", specifiedCounty.getMetrics().getVaccinationsInitiatedRatio());
        model.addAttribute ("vaccinationsCompletedRatio", specifiedCounty.getMetrics().getVaccinationsCompletedRatio());

        //line chart new cases/new death
        List<Actuals>  specifiedCountyActualsTimeseries = covidDataService.getSpecifiedCountyActualsTimeseries();

        Map<String, Integer> newCasesMap = new HashMap<>();
        Map<String, Integer> newDeathMap = new HashMap<>();
        Map<String, Integer> totalCasesMap = new HashMap<>();
        Map<String, Integer> totalDeathMap = new HashMap<>();

        for(Actuals actuals : specifiedCountyActualsTimeseries){
            System.out.println(actuals.getDate() + ": " + actuals.getDeaths());
            newCasesMap.put(actuals.getDate(), actuals.getNewCases());
            newDeathMap.put(actuals.getDate(), actuals.getNewDeaths());
            totalCasesMap.put(actuals.getDate(), actuals.getCases());
            totalDeathMap.put(actuals.getDate(), actuals.getDeaths());
        }

        model.addAttribute("Dates", newDeathMap.keySet());
        model.addAttribute("NewCases", newCasesMap.values());
        model.addAttribute("NewDeaths", newDeathMap.values());
        model.addAttribute("TotalCases", totalCasesMap.values());
        model.addAttribute("TotalDeaths", totalDeathMap.values());

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


        return "newCaseLineChart";
    }


}
