package com.agoda.downloader.domain;

import org.junit.Test;

import java.net.MalformedURLException;

import static org.junit.Assert.*;

/**
 * Created by hrishikeshshinde on 27/11/16.
 */
public class FileResourceTest {

    @Test
    public void getResourceFileName() throws MalformedURLException {
        String source = "https://s3.ap-south-1.amazonaws.com/hriships/HrishikeshShinde_resume.pdf";
        FileResource fileResource = new FileResource(source);

        String filename = fileResource.getFilename();
        assertTrue(filename != null && filename.equals("HrishikeshShinde_resume.pdf"));
    }
}