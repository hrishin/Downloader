package com.agoda.downloader.service;

import com.agoda.downloader.domain.DownloadState;
import com.agoda.downloader.domain.FileResource;

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
        this.downloader = getDownloader(this.fileResource);
        this.downloadPath = downloadPath;
    }

    public DownloadActivity(FileResource fileResource, Downloader downloader, String downloadPath) {
        this.fileResource = fileResource;
        this.downloader = downloader;
        this.downloadPath = downloadPath;
    }

    private Downloader getDownloader(FileResource fileResource) {
        switch (fileResource.getProtocol()) {
            case "http":
            case "https":
                return new HttpDownloader();

            case "ftp":
            case "sftp":
                return new FtpDownloader();

            default:
                return null;
        }
    }

    public Callable<DownloadState> getCallable() {
        Callable<DownloadState> downloadCallable = new Callable<DownloadState>() {
            @Override
            public DownloadState call() throws Exception {
                downloader.download(fileResource.getBaseURL(), downloadPath, fileResource.getFilename());
                return downloader.getStatus();
            }
        };

        return downloadCallable;
    }
}
