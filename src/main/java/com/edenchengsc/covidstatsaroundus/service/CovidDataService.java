package com.edenchengsc.covidstatsaroundus.service;

import com.edenchengsc.covidstatsaroundus.CovidStatsAroundUsApplication;
import com.edenchengsc.covidstatsaroundus.models.Actuals;
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
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CovidDataService {

    public String FILE_DIR = "C:\\Users\\edenchengshu\\OneDrive\\Documents\\GitHub\\covidstatsaroundus\\jsonFile";
    private static String APIKEY = "e2592af2a51a42f6acedbe547a95e0da";

    private static final Logger log = LoggerFactory.getLogger(CovidStatsAroundUsApplication.class);

    private List<County> allCountyStats = new ArrayList<>();

    public County getSpecifiedCounty() {
        return specifiedCounty;
    }

    public void setSpecifiedCounty(County specifiedCounty) {
        this.specifiedCounty = specifiedCounty;
    }

    private County specifiedCounty = new County();

    public List<Actuals> getSpecifiedCountyActualsTimeseries() {
        return specifiedCountyActualsTimeseries;
    }

    public void setSpecifiedCountyActualsTimeseries(List<Actuals> specifiedCountyActualsTimeseries) {
        this.specifiedCountyActualsTimeseries = specifiedCountyActualsTimeseries;
    }

    public List<Actuals> specifiedCountyActualsTimeseries = new ArrayList<>();

    public List<County> getCounties(){
        return allCountyStats;
    }

    private List<State> allStateStats = new ArrayList<>();

    public List<State> getStateStats(){
        return allStateStats;
    }


    public Map<String, String> apiURL_Files = new HashMap<>();

    private void setApiFiles() {
       //this.apiURL_Files.put("single_county_summary.json", "https://api.covidactnow.org/v2/county/{fips}.json?apiKey={apiKey}");
        this.apiURL_Files.put("Single_County_Timeseries.json", "https://api.covidactnow.org/v2/county/{fips}.timeseries.json?apiKey={apiKey}");
    }

    private String formatURL(String url){
        if(url.contains("{fips}")){
            url = url.replace("{fips}", "53033"); // 53033 king county
        }
        return url.replace("{apiKey}", APIKEY);
    }

    @Bean
    public CommandLineRunner run() throws Exception {
        return args -> {
            setApiFiles();
            checkJsonFiles();

            // Single county data
            try {
                ObjectMapper mapper = new ObjectMapper();
                String fileName = FILE_DIR + File.separator + "Single_County_Timeseries.json";
                InputStream jsonFileStream = new FileInputStream(fileName);
                County county = (County) mapper.readValue(jsonFileStream, County.class);
                log.info(county.toString());
                this.specifiedCounty = county;
                this.specifiedCountyActualsTimeseries = Arrays.stream(county.getActualsTimeseries())
                        .filter(a -> a.getDate().startsWith("2021-05"))
                        .collect(Collectors.toList());
                log.info("specifiedCountyActualsTimeseries : " + this.specifiedCountyActualsTimeseries.size());
            } catch (Exception e){
                log.info("Exceptions here: " + e.toString());
            }

            //Single County Timeseries

        };
    }

    /*
     Check if the .json files are local ready.
     If not or too old, download again
     */
    public void checkJsonFiles() throws IOException
    {
        //String sourceURL, String targetDirectory, String fileName
        for(String fileName : this.apiURL_Files.keySet()){
            File file = new File(FILE_DIR + File.separator + fileName);
            if(file.exists()){
                long diff = new Date().getTime() - file.lastModified();
                if (diff < 24){
                     System.out.println("Latest version exists: " + fileName);
                     continue;
                }
            }
            URL url = new URL(formatURL(this.apiURL_Files.get(fileName)));
            Path targetPath = file.toPath();
            if(file.createNewFile()){
                System.out.println(targetPath +" File Created");
            }else {
                System.out.println("File "+ targetPath +" already exists");
            }
            Files.copy(url.openStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
