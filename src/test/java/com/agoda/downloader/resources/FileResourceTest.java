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
        assertTrue(filename.contains(expectedFileName));
    }

    @Test
    public void protocolSupport() throws MalformedURLException {
        FileResource fileResource1 = new FileResource("https://s3.ap-south-1.amazonaws.com");
        FileResource fileResource2 = new FileResource("http://s3.ap-south-1.amazonaws.com");
        FileResource fileResource3 = new FileResource("ftp://s3.ap-south-1.amazonaws.com");
        FileResource fileResource4 = new FileResource("sftp://s3.ap-south-1.amazonaws.com");
    }

    @Test(expected = MalformedURLException.class)
    public void unsupportedProtocol() throws MalformedURLException {
        FileResource fileResource1 = new FileResource("vmfs://s3.ap-south-1.amazonaws.com");
    }
}