package com.agoda.downloader.domain;

import org.junit.Test;

import java.net.MalformedURLException;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Created by hrishikeshshinde on 27/11/16.
 */
public class FileResourceTest {

    @Test
    public void getResourceFileName() throws MalformedURLException {
        String source = "https://s3.ap-south-1.amazonaws.com/hriships/HrishikeshShinde_resume.pdf";
        FileResource fileResource1 = new FileResource(source);
        FileResource fileResource2 = new FileResource(source);

        String filename1 = fileResource1.getFilename();
        String filename2 = fileResource2.getFilename();
        assertFalse(filename1.equals(filename2));
    }

    @Test
    public void test() {
        System.out.print(UUID.randomUUID().toString());
    }
}