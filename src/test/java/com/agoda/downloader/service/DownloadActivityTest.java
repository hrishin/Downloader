package com.agoda.downloader.service;

import com.agoda.downloader.domain.DownloadState;
import com.agoda.downloader.domain.FileResource;
import org.junit.Test;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import static com.agoda.downloader.domain.DownloadState.COMPLETED;
import static org.junit.Assert.assertEquals;

/**
 * Created by hrishikeshshinde on 01/12/16.
 */
public class DownloadActivityTest {

    @Test
    public void callableHttpDownloadTask() throws MalformedURLException, ExecutionException, InterruptedException {
        String source = "https://s3.ap-south-1.amazonaws.com/hriships/HrishikeshShinde_resume.pdf";
        String path = "/Users/hrishikeshshinde/downloadtest/";
        FileResource fileResource = new FileResource(source);
        DownloadActivity activity = new DownloadActivity(fileResource, path);
        FutureTask<DownloadState> downloadTask = new FutureTask<DownloadState>(activity.getCallable());
        new Thread(downloadTask).run();

        assertEquals(COMPLETED, downloadTask.get());

    }

    @Test
    public void callableFtpDownloadTask() throws MalformedURLException, ExecutionException, InterruptedException {
        String source = "ftp://anonymous:myemailname@ftp.hq.nasa.gov/pub/astrophysics/FTP_Instructions.txt";
        String path = "/Users/hrishikeshshinde/downloadtest/";
        FileResource fileResource = new FileResource(source);
        DownloadActivity activity = new DownloadActivity(fileResource, path);
        FutureTask<DownloadState> downloadTask = new FutureTask<DownloadState>(activity.getCallable());
        new Thread(downloadTask).run();

        assertEquals(COMPLETED, downloadTask.get());

    }
}