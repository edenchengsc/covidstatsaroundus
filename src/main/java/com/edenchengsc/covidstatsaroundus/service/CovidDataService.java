package com.edenchengsc.covidstatsaroundus.service;

import com.edenchengsc.covidstatsaroundus.CovidStatsAroundUsApplication;
import com.edenchengsc.covidstatsaroundus.models.County;
import com.edenchengsc.covidstatsaroundus.models.Metrics;
import com.edenchengsc.covidstatsaroundus.models.State;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
public class CovidDataService {

    private static final Logger log = LoggerFactory.getLogger(CovidStatsAroundUsApplication.class);

    private List<County> allCountyStats = new ArrayList<>();

    public List<County> getCounties(){
        return allCountyStats;
    }

    private List<State> allStateStats = new ArrayList<>();

    public List<State> getStateStats(){
        return allStateStats;
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
        return args -> {

            download("https://api.covidactnow.org/v2/state/WA.timeseries.csv?apiKey=e2592af2a51a42f6acedbe547a95e0da",
                    "C:\\Users\\edenchengshu\\OneDrive\\Desktop\\EdenProjectFile",
                    "Single_State_Timeseries_CSV.csv");
            download("https://api.covidactnow.org/v2/states.csv?apiKey=e2592af2a51a42f6acedbe547a95e0da",
                    "C:\\Users\\edenchengshu\\OneDrive\\Desktop\\EdenProjectFile",
                    "All_States_Summary_CSV.csv");

            download("https://api.covidactnow.org/v2/county/WA.json?apiKey=e2592af2a51a42f6acedbe547a95e0da",
                    "C:\\Users\\edenchengshu\\OneDrive\\Desktop\\EdenProjectFile",
                    "county_summary.json");

//            List<County> counties = new ArrayList<>();
//            Reader reader = Files.newBufferedReader(Paths.get("C:\\Users\\edenchengshu\\OneDrive\\Desktop\\EdenProjectFile\\All_States_Summary_CSV.csv"));
//            CSVParser csvParser = new CSVParser(reader, CSVFormat.EXCEL.withHeader());
//
//            for (CSVRecord record : csvParser) {
//                //if (record.get("county").equals("King County")) {
//                    County county = new County();
//
//                    county.setCounty(record.get("state"));
//                    county.setLastUpdatedDate(record.get("lastUpdatedDate"));
//                    Metrics metrics = new Metrics();
//                    metrics.setVaccinationsCompletedRatio(Float.parseFloat(record.get("metrics.vaccinationsCompletedRatio")));
//                    county.setMetrics(metrics);
//                    counties.add(county);
//                    System.out.println(county.getLastUpdatedDate());
//               // }
//            }
//            this.allCountyStats = counties;


            try {
                ObjectMapper mapper = new ObjectMapper();
                String fileName = "C:\\Users\\edenchengshu\\OneDrive\\Desktop\\EdenProjectFile\\county_summary.json";
                InputStream jsonFileStream = new FileInputStream(fileName);
                County[] countyStats = (County[]) mapper.readValue(jsonFileStream, County[].class);

                log.info(countyStats.toString());
                allCountyStats = Arrays.asList(countyStats.clone());
            } catch (Exception e){
                log.info("Exceptions here: " + e.toString());
            }

//            //handle json
//            County[] countyStats = restTemplate.getForObject(
//                    "https://api.covidactnow.org/v2/county/WA.json?apiKey=e2592af2a51a42f6acedbe547a95e0da", County[].class);
//            log.info(countyStats.toString());
//            allCountyStats = Arrays.asList(countyStats.clone());
//            State[]  stateStats = restTemplate.getForObject(
//                    "https://api.covidactnow.org/v2/states.json?apiKey=e2592af2a51a42f6acedbe547a95e0da", State[].class);
//            log.info(stateStats.toString());
//            allStateStats = Arrays.asList(stateStats.clone());
        };
    }

    private static Path download(String sourceURL, String targetDirectory, String fileName) throws IOException
    {
        URL url = new URL(sourceURL);
       //String fileName = sourceURL.substring(sourceURL.lastIndexOf('/') + 1, sourceURL.lastIndexOf('?'));
        File file = new File(targetDirectory + File.separator + fileName);
        Path targetPath = file.toPath();
        if(file.createNewFile()){
            System.out.println(targetPath +" File Created");
        }else {
            System.out.println("File "+ targetPath +" already exists");
        }

        Files.copy(url.openStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        return targetPath;
    }
}
