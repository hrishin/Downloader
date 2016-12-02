package com.agoda.downloader.service;

import com.agoda.downloader.DownloadSetup;
import com.agoda.downloader.domain.FileResource;
import com.agoda.downloader.protocols.Downloader;
import org.junit.Test;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import static com.agoda.downloader.domain.DownloadState.COMPLETED;
import static org.junit.Assert.assertEquals;

/**
 * Created by hrishikeshshinde on 01/12/16.
 */
public class DownloadActivityTest extends DownloadSetup {

    @Test
    public void callableHttpDownloadTask() throws MalformedURLException, ExecutionException, InterruptedException {
        FileResource fileResource = new FileResource(httpResource);
        DownloadActivity activity = new DownloadActivity(fileResource, downloadLocation);
        FutureTask<DownloadActivity> downloadTask = new FutureTask<DownloadActivity>(activity.getDownloadCall());
        new Thread(downloadTask).run();

        assertEquals(COMPLETED, downloadTask.get().getStatus());

    }

    @Test
    public void callableFtpDownloadTask() throws MalformedURLException, ExecutionException, InterruptedException {
        FileResource fileResource = new FileResource(ftpResource);
        DownloadActivity activity = new DownloadActivity(fileResource, downloadLocation);
        FutureTask<DownloadActivity> downloadTask = new FutureTask<DownloadActivity>(activity.getDownloadCall());
        new Thread(downloadTask).run();

        assertEquals(COMPLETED, downloadTask.get().getStatus());

    }
}