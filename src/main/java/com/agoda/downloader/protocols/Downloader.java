package com.agoda.downloader.protocols;

import com.agoda.downloader.exceptions.DownloadException;

/**
 * Download interface to abstract the download behaviour
 */
public interface Downloader {

    DownloadState download(String source, String path, String fileName) throws DownloadException;
    DownloadState getStatus();
}
