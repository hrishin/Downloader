package com.agoda.downloader.service;

import com.agoda.downloader.domain.DownloadState;
import com.agoda.downloader.domain.FileResource;
import com.agoda.downloader.protocols.Downloader;

import java.util.concurrent.Callable;

/**
 * Created by hrishikeshshinde on 01/12/16.
 */

public class DownloadActivity {
    private final FileResource fileResource;
    private final Downloader downloader;
    private final String downloadPath;

    public DownloadActivity(FileResource fileResource, String downloadPath) {
        this.fileResource = fileResource;
        this.downloader = this.fileResource.createDownloader();
        this.downloadPath = downloadPath;
    }

    public DownloadActivity(FileResource fileResource, Downloader downloader, String downloadPath) {
        this.fileResource = fileResource;
        this.downloader = downloader;
        this.downloadPath = downloadPath;
    }

    public Callable<DownloadActivity> getDownloadCall() {
        DownloadActivity currentActvity;
        Callable<DownloadActivity> downloadCallable = new Callable<DownloadActivity>() {
            @Override
            public DownloadActivity call() throws Exception {
                downloader.download(fileResource.getBaseURL(), downloadPath, fileResource.getFilename());
                return DownloadActivity.this;
            }
        };

        return downloadCallable;
    }

    public DownloadState getStatus() {
        return this.downloader.getStatus();
    }
}
