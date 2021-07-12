package com.edenchengsc.covidstatsaroundus.service;

import com.edenchengsc.covidstatsaroundus.CovidStatsAroundUsApplication;
import com.edenchengsc.covidstatsaroundus.models.Actuals;
import com.edenchengsc.covidstatsaroundus.models.County;
import com.edenchengsc.covidstatsaroundus.models.State;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CovidDataService {

    private static final Logger log = LoggerFactory.getLogger(CovidStatsAroundUsApplication.class);

    private static String FILE_DIR = "./jsonFile";
    private static String APIKEY = "e2592af2a51a42f6acedbe547a95e0da";
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static Map<String, String> countyList = new HashMap<>();
    private List<County> allCountyStats = new ArrayList<>();

    private String fips = "";
    public String getFips() {
        return fips;
    }

    public void setFips(String fips) {
        this.fips = fips;
    }

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

    @Bean
    public CommandLineRunner run() throws Exception {
        return args -> {
            setApiFiles();
            checkJsonFiles();
            getCountyData(fips);

            //Defaut List of County
            countyList.put("King County", "WA");
            countyList.put("Orange County", "CA");
            countyList.put("New York County", "NY");
            countyList.put("Honolulu County", "HI");
            setCountyTableContent();

            //All states stats
            loadAllStateStats();
        };
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    private void setApiFiles() {
        this.apiURL_Files.put("Single_County_Timeseries.json", "https://api.covidactnow.org/v2/county/{fips}.timeseries.json?apiKey={apiKey}");
        this.apiURL_Files.put("All_County_Summary.json", "https://api.covidactnow.org/v2/counties.json?apiKey={apiKey}");
        this.apiURL_Files.put("All_State_Summary.json", "https://api.covidactnow.org/v2/states.json?apiKey={apiKey}");
    }

    private String formatURL(String url){
        if(url.contains("{fips}")){
            url = url.replace("{fips}", "53033"); // 53033 king county
        }
        return url.replace("{apiKey}", APIKEY);
    }

    public void getCountyData(String fips){
        try {
            if(fips != ""){
                //get live
                RestTemplate restTemplate = new RestTemplate();
                String apiURL = "https://api.covidactnow.org/v2/county/" + fips + ".timeseries.json?apiKey=e2592af2a51a42f6acedbe547a95e0da";
                System.out.println("This is " + apiURL);
                County countyStats = restTemplate.getForObject(
                        apiURL, County.class);
                log.info(countyStats.toString());
                this.specifiedCounty = countyStats;

                this.specifiedCountyActualsTimeseries = Arrays.stream(countyStats.getActualsTimeseries())
                        .filter(a -> in30Days(a.getDate()))
                        .collect(Collectors.toList());
                log.info("specifiedCountyActualsTimeseries : " + this.specifiedCountyActualsTimeseries.size());

                //add to the comparison table if not included.
                if(!countyList.containsKey(countyStats.getCounty())){
                    countyList.put(countyStats.getCounty(), countyStats.getState());
                    //reset to empty
                    this.allCountyStats = new ArrayList<>();
                    setCountyTableContent();
                }
            } else {
                // Single county data
                ObjectMapper mapper = new ObjectMapper();
                String fileName = FILE_DIR + File.separator + "Single_County_Timeseries.json";
                InputStream jsonFileStream = new FileInputStream(fileName);
                County county = (County) mapper.readValue(jsonFileStream, County.class);
                log.info(county.toString());
                this.specifiedCounty = county;

                this.specifiedCountyActualsTimeseries = Arrays.stream(county.getActualsTimeseries())
                        .filter(a -> in30Days(a.getDate()))
                        .collect(Collectors.toList());
                log.info("specifiedCountyActualsTimeseries : " + this.specifiedCountyActualsTimeseries.size());
            }
        } catch (Exception e){
            log.info("Exceptions here: " + e.toString());
        }
    }

    public void setCountyTableContent(){
        try {
            ObjectMapper mapper = new ObjectMapper();
            String fileName = FILE_DIR + File.separator + "All_County_Summary.json";
            InputStream jsonFileStream = new FileInputStream(fileName);
            County[] counties = (County[]) mapper.readValue(jsonFileStream, County[].class);
            log.info(counties.toString());
            for(County county : counties){
                if(countyList.keySet().contains(county.getCounty()) && county.getState().equals(countyList.get(county.getCounty()))){
                    this.allCountyStats.add(county);
                }
            }
            log.info("allCountyStats size : " + this.allCountyStats.size());
        } catch (Exception e){
            log.info("Exceptions here: " + e.toString());
        }
    }

    private void loadAllStateStats() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String fileName = FILE_DIR + File.separator + "All_State_Summary.json";
        InputStream jsonFileStream = new FileInputStream(fileName);
        State[] states = (State[]) mapper.readValue(jsonFileStream, State[].class);
        log.info(states.toString());
        this.allStateStats = Arrays.asList(states);
    }

    private boolean in30Days(String datetime) {
        Date today = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(today);
        cal.add(Calendar.DAY_OF_MONTH, -31);
        Date today30 = cal.getTime();
        Date dataDate;

        try {
            dataDate = sdf.parse(datetime);
        }catch (Exception e){
            return false;
        }

        return today30.before(dataDate);
    }

    /*
     Check if the .json files are local ready.
     If not or too old, download again
     */

    public void checkJsonFiles() throws IOException {
        String dirPath = new File("").getAbsolutePath() + File.separator + "jsonFile";
        File dir = new File(dirPath);
        if(!dir.exists()){
            dir.mkdir();
            System.out.println("Create dir:" + dirPath);
        }

        //String sourceURL, String targetDirectory, String fileName
        for(String fileName : this.apiURL_Files.keySet()){
            File file = new File(FILE_DIR + File.separator + fileName);
            if(file.exists()){
                String fileDate =  sdf.format(file.lastModified());
                String today = sdf.format(new Date().getTime());
                if (today.equals(fileDate)){
                     System.out.println("Latest version exists: " + fileDate);
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
