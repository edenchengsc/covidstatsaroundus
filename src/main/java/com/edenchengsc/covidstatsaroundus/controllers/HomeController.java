package com.edenchengsc.covidstatsaroundus.controllers;

import com.edenchengsc.covidstatsaroundus.models.Actuals;
import com.edenchengsc.covidstatsaroundus.models.County;
import com.edenchengsc.covidstatsaroundus.models.State;
import com.edenchengsc.covidstatsaroundus.service.CovidDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

@Controller
public class HomeController {

    private static DecimalFormat df2 = new DecimalFormat("#.#");

    @Autowired
    CovidDataService covidDataService;


    @GetMapping("/")
    public String home(Model model, @RequestParam (value = "fips", required = false) String fips) throws IOException {

        covidDataService = new CovidDataService();
        if(fips != null && fips != ""){
            covidDataService.getCountyData(fips);
        }

        //Single County data
        County specifiedCounty = covidDataService.getSpecifiedCounty();
        model.addAttribute ("countyName", specifiedCounty.getCounty());
        model.addAttribute ("stateName", specifiedCounty.getState());
        model.addAttribute ("lastUpdatedDate", specifiedCounty.getLastUpdatedDate());

        //Comparation table
        List<County> allCountyStats = covidDataService.getCounties();
        model.addAttribute("selectedCountyStats", allCountyStats);

        //line chart new cases/new death
        List<Actuals>  specifiedCountyActualsTimeseries = covidDataService.getSpecifiedCountyActualsTimeseries();
        Collections.sort(specifiedCountyActualsTimeseries, new Comparator<Actuals>() {
            @Override
            public int compare(Actuals o1, Actuals o2) {
                return  Integer.parseInt(o1.getDate().replace("-", "")) - Integer.parseInt(o2.getDate().replace("-", ""));
            }
        });

        double newConfirmedCases7DaysSum = 0;
        double dailyDeath7DaysSum = 0;

        String[] dataArr = new String[30];
        int[] newCasesNumArr = new int[30];
        int[] newDeathNumArr = new int[30];
        int[] totalCasesNumArr = new int[30];
        int[] totalDeathNumArr = new int[30];

        int i = 0;
        for(Actuals actuals : specifiedCountyActualsTimeseries){
            //System.out.println(actuals.getDate() + ": " + actuals.getDeaths());
            dataArr[i] = actuals.getDate();
            newCasesNumArr[i] = actuals.getNewCases();
            newDeathNumArr[i] = actuals.getNewDeaths();
            totalCasesNumArr[i] = actuals.getCases();
            totalDeathNumArr[i] = actuals.getDeaths();
            //System.out.println( "i = " + i + "  ->"+dataArr[i]  + ": " + newDeathNumArr[i]);
            i++;
            if(i >= 30){
                break;
            }
            if(i >= 23){
                //System.out.println("New Cases :" + actuals.getNewCases() + "  New Death: " +actuals.getNewDeaths() );
                newConfirmedCases7DaysSum += actuals.getNewCases();
                dailyDeath7DaysSum += actuals.getNewDeaths();
            }
        }

        model.addAttribute ("newConfirmedCasesPerDay", df2.format(newConfirmedCases7DaysSum/7));
        model.addAttribute ("aveDailyDeath", df2.format(dailyDeath7DaysSum/7));
        model.addAttribute ("oneDoseVaccinatedRatio", specifiedCounty.getMetrics().getVaccinationsInitiatedRatio()*10000/100 + "%");
        model.addAttribute ("fullVaccinatedRatio",  specifiedCounty.getMetrics().getVaccinationsCompletedRatio()*10000/100+ "%");

        model.addAttribute("Dates", dataArr);
        model.addAttribute("NewCases", newCasesNumArr);
        model.addAttribute("NewDeaths", newDeathNumArr);
        model.addAttribute("TotalCases", totalCasesNumArr);
        model.addAttribute("TotalDeaths", totalDeathNumArr);

        //Vaccine Bar Chart
        List<State> allStateStats = covidDataService.getStateStats();
        Collections.sort(allStateStats, new Comparator<State>() {
            @Override
            public int compare(State o1, State o2) {
                return Math.round((o2.getMetrics().getVaccinationsCompletedRatio() - o1.getMetrics().getVaccinationsCompletedRatio()) * 100);
            }
        });
        int size = allStateStats.size();
        String[] stateArr = new String[size];
        int[]  initRatioPercentage = new int[size];
        int[]  fullRatioPercentage = new int[size];

        int j = 0;
        for(State state : allStateStats){
            stateArr[j] = state.getState();
            initRatioPercentage[j] = Math.round(state.getMetrics().getVaccinationsInitiatedRatio()*10000/100);
            fullRatioPercentage[j] = Math.round(state.getMetrics().getVaccinationsCompletedRatio()*10000/100);
            j++;

            //System.out.println("State: " + state.getState() + "  - " + Math.round(state.getMetrics().getVaccinationsCompletedRatio()*100) );
        }

        model.addAttribute("keySet", stateArr);
        model.addAttribute("vaccinationsInitiatedRatioValues", initRatioPercentage);
        model.addAttribute("vaccinationsCompletedRatioValues", fullRatioPercentage);

        return "index";
    }
}
