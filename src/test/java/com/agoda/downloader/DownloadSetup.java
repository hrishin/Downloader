package com.agoda.downloader;

import org.junit.Before;

/**
 * Created by hrishikesh_shinde on 12/1/2016.
 */
public class DownloadSetup {

    protected String downloadLocation;
    protected String httpResource;
    protected String ftpResource;

    @Before
    public void setup() {
        downloadLocation = System.getenv("DOWNLOAD_LOCATION");
        downloadLocation = downloadLocation.endsWith("/") ? downloadLocation : downloadLocation+"/";
        httpResource = "https://s3.ap-south-1.amazonaws.com/hriships/HrishikeshShinde_resume.pdf";
        ftpResource = "ftp://anonymous:myemailname@ftp.hq.nasa.gov/pub/astrophysics/FTP_Instructions.txt";
    }

}
