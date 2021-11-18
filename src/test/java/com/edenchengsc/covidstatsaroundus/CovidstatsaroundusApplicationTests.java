package com.edenchengsc.covidstatsaroundus;

import com.edenchengsc.covidstatsaroundus.service.CovidDataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CovidstatsaroundusApplicationTests {

//	@Test
//	void contextLoads() {
//	}

    @Autowired
    CovidDataService covidDataService;

    @Test
    void checkJsonFilesTest() throws IOException {
        covidDataService.checkJsonFiles();
        String dirPath = new File("").getAbsolutePath() + File.separator + "jsonFile";
        File directory =new File(dirPath);
        for(String s : directory.list()){
            System.out.println("File name:"+ s);
        }
        int fileCount=directory.list().length;
        assertThat(fileCount == covidDataService.apiURL_Files.size());

    }

}
