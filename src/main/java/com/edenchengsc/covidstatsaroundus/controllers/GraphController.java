package com.edenchengsc.covidstatsaroundus.controllers;

import com.edenchengsc.covidstatsaroundus.models.County;
import com.edenchengsc.covidstatsaroundus.models.RiskMap;
import com.edenchengsc.covidstatsaroundus.models.State;
import com.edenchengsc.covidstatsaroundus.service.CovidDataService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


import java.io.FileWriter;
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

        return "vaccineStateBarChart";
    }

    @GetMapping("/riskMapChart")
    public String riskMapChart(Model model) throws JsonProcessingException {
//        List<State> allStateStats = covidDataService.getStateStats();
//
//        Map<String, Integer> riskLevelAllState = new LinkedHashMap<>();
//
//        List<RiskMap> reislList = new ArrayList<>();
//
//        for(State state : allStateStats){
//            System.out.println("State: " + state.getState() + "  - " + state.getRiskLevels().getOverall());
//            riskLevelAllState.put(state.getState(), state.getRiskLevels().getOverall());
//
//            RiskMap rs = new RiskMap();
//            rs.setCode(state.getState());
//            rs.setValue(state.getRiskLevels().getOverall());
//            reislList.add(rs);
//        }

//
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String json = "";
//        try {
//            json= objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(reislList);
//            System.out.println(json);
//        } catch(Exception e) {
//            e.printStackTrace();
//        }



        return "riskMapChart";


    }


}