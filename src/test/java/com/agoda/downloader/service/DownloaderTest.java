package com.agoda.downloader.service;

import com.agoda.downloader.domain.FileResource;
import org.junit.Test;

import java.net.MalformedURLException;

/**
 * Created by hrishikeshshinde on 27/11/16.
 */
public class DownloaderTest {

    @Test
    public void downloadFromHttp() throws MalformedURLException {
        String source = "https://s3.ap-south-1.amazonaws.com/hriships/HrishikeshShinde_resume.pdf";
        FileResource fileResource = new FileResource(source);
        String path = "~/";
        fileResource.download(path);

    }
}
