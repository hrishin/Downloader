package com.agoda.downloader.protocols;

import com.agoda.downloader.exceptions.DownloadException;

/**
 * Created by hrishikeshshinde on 29/11/16.
 */
public interface Downloader {

    DownloadState download(String source, String path, String fileName) throws DownloadException;
    DownloadState getStatus();
}
