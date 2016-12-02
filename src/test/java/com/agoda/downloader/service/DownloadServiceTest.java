package com.agoda.downloader.service;

import com.agoda.downloader.ConfigLoader;
import com.agoda.downloader.DownloadSetup;
import com.agoda.downloader.domain.DownloadState;
import com.agoda.downloader.exception.ConfigurationException;
import com.agoda.downloader.exception.DownloadException;
import com.agoda.downloader.protocols.FtpDownloader;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

/**
 * Created by hrishikeshshinde on 01/12/16.
 */
public class DownloadServiceTest extends DownloadSetup {

    private final static Logger LOGGER = Logger.getLogger(DownloadServiceTest.class.getName());

    @Test
    public void appTest1() throws ParseException, ConfigurationException {
        String[] args = {"-dl", "/Users/hrishikeshshinde/downloadtest/",
                        "-s", httpResource + "," + ftpResource};
        ConfigLoader configLoader = new ConfigLoader(args);
        DownloadService downloadService = new DownloadService(configLoader.getSources(),
                                                                configLoader.getDownloadLocation());
        downloadService.startDownloads();

        assertResult(downloadService);
    }

    private void assertResult(DownloadService downloadService) {
        List<DownloadActivity> downloadActivities = downloadService.getDownloadActivities();
        for (int i=0; i < downloadActivities.size(); i++) {
            try {
                DownloadActivity downloadActivity = downloadService.getUpdatedActivity();
                Assert.assertEquals(DownloadState.COMPLETED, downloadActivity.getStatus());
            } catch (DownloadException e) {
                LOGGER.warning(e.getMessage());
            }
        }
    }

}