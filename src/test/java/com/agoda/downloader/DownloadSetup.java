package com.agoda.downloader;

import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by hrishikesh_shinde on 12/1/2016.
 */
public class DownloadSetup {

    protected String downloadLocation;
    protected String httpResource;
    protected String ftpResource;

    @Before
    public void setup() {
        File test = new File("downloaddir");
        test.mkdir();
        downloadLocation = test.getAbsolutePath()+"/";
        httpResource = "https://s3.ap-south-1.amazonaws.com/hriships/HrishikeshShinde_resume.pdf";
        ftpResource = "ftp://anonymous:myemailname@ftp.hq.nasa.gov/pub/astrophysics/FTP_Instructions.txt";
    }

    @After
    public void cleanup() {
        try {
            File directoryFile = new File(downloadLocation);

            for (File file : directoryFile.listFiles()) {
                FileDeleteStrategy.FORCE.delete(file);
            }
            FileUtils.deleteDirectory(directoryFile);
        } catch (IOException e) {

        }
    }

}
