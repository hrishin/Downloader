package com.agoda.downloader.service;

import com.agoda.downloader.DownloadSetup;
import com.agoda.downloader.exceptions.DownloadException;
import com.agoda.downloader.protocols.FtpDownloader;
import com.agoda.downloader.protocols.HttpDownloader;
import com.agoda.downloader.resources.FileResource;
import org.junit.Test;
import org.mockito.Mockito;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import static com.agoda.downloader.protocols.DownloadState.COMPLETED;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by hrishikeshshinde on 01/12/16.
 */
public class DownloadActivityTest extends DownloadSetup {

    @Test
    public void callableHttpDownloadTask() throws MalformedURLException, ExecutionException, InterruptedException, DownloadException {
        FileResource fileResource = new FileResource(httpResource);

        /*mock downloader*/
        HttpDownloader downloader = Mockito.mock(HttpDownloader.class);
        when(downloader.download(fileResource.getBaseURL(), downloadLocation, fileResource.getFilename()))
                .thenReturn(COMPLETED);
        when(downloader.getStatus())
                .thenReturn(COMPLETED);

        DownloadActivity activity = new DownloadActivity(fileResource, downloader, downloadLocation);
        FutureTask<DownloadActivity> downloadTask = new FutureTask<DownloadActivity>(activity.getDownloadCall());
        new Thread(downloadTask).run();

        assertEquals(COMPLETED, downloadTask.get().getStatus());

    }

    @Test
    public void callableFtpDownloadTask() throws MalformedURLException, ExecutionException, InterruptedException, DownloadException {
        FileResource fileResource = new FileResource(ftpResource);

        /*mock downloader*/
        FtpDownloader downloader = Mockito.mock(FtpDownloader.class);
        when(downloader.download(fileResource.getBaseURL(), downloadLocation, fileResource.getFilename()))
                .thenReturn(COMPLETED);
        when(downloader.getStatus())
                .thenReturn(COMPLETED);

        DownloadActivity activity = new DownloadActivity(fileResource, downloader, downloadLocation);
        FutureTask<DownloadActivity> downloadTask = new FutureTask<DownloadActivity>(activity.getDownloadCall());
        new Thread(downloadTask).run();

        assertEquals(COMPLETED, downloadTask.get().getStatus());

    }
}