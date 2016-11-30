package com.agoda.downloader.service;

import com.agoda.downloader.domain.FileResource;
import com.agoda.downloader.domain.DownloadState;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

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
        DownloadState status = downloader.download(source, path, fileName);
        Assert.assertTrue(status == DownloadState.COMPLETED);
    }


}
