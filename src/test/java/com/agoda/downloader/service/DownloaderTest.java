package com.agoda.downloader.service;

import com.agoda.downloader.domain.FileResource;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created by hrishikeshshinde on 27/11/16.
 */
public class DownloaderTest {

    @Test
    public void downloadFromHttp() throws IOException {
        String source = "https://s3.ap-south-1.amazonaws.com/hriships/HrishikeshShinde_resume.pdf";
        String path = "/Users/hrishikeshshinde/";
        FileResource fileResource =  new FileResource(source);
        String fileName = fileResource.getFilename();

        Downloader downloader = new HttpDownloader();
        boolean status = downloader.download(source, path, fileName);
        Assert.assertTrue(status);
    }
}
