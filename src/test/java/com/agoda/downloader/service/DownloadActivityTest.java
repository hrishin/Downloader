package com.agoda.downloader.service;

import com.agoda.downloader.domain.DOWNLOAD_STATE;
import com.agoda.downloader.domain.FileResource;
import org.junit.Test;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import static com.agoda.downloader.domain.DOWNLOAD_STATE.COMPLETED;
import static org.junit.Assert.assertEquals;

/**
 * Created by hrishikeshshinde on 01/12/16.
 */
public class DownloadActivityTest extends TestSetup {

    @Test
    public void callableHttpDownloadTask() throws MalformedURLException, ExecutionException, InterruptedException {
        FileResource fileResource = new FileResource(httpResource);
        DownloadActivity activity = new DownloadActivity(fileResource, downloadLocation);
        FutureTask<DOWNLOAD_STATE> downloadTask = new FutureTask<DOWNLOAD_STATE>(activity.getCallable());
        new Thread(downloadTask).run();

        assertEquals(COMPLETED, downloadTask.get());

    }

    @Test
    public void callableFtpDownloadTask() throws MalformedURLException, ExecutionException, InterruptedException {
        FileResource fileResource = new FileResource(ftpResource);
        DownloadActivity activity = new DownloadActivity(fileResource, downloadLocation);
        FutureTask<DOWNLOAD_STATE> downloadTask = new FutureTask<DOWNLOAD_STATE>(activity.getCallable());
        new Thread(downloadTask).run();

        assertEquals(COMPLETED, downloadTask.get());

    }
}