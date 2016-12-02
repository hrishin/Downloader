package com.agoda.downloader.resources;

import org.junit.Test;

import java.net.MalformedURLException;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

/**
 * Created by hrishikeshshinde on 27/11/16.
 */
public class FileResourceTest {

    @Test
    public void getResourceFileName() throws MalformedURLException {
        String source = "https://s3.ap-south-1.amazonaws.com/hriships/HrishikeshShinde_resume.pdf";
        String expectedFileName = "HrishikeshShinde_resume.pdf";

        FileResource fileResource = new FileResource(source);

        String filename = fileResource.getFilename();
        assertTrue(filename.equals(expectedFileName));
    }

    @Test
    public void test() {
        System.out.print(UUID.randomUUID().toString());
    }
}