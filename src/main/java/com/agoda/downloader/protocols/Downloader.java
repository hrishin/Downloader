package com.agoda.downloader.protocols;

import com.agoda.downloader.domain.DownloadState;
import com.agoda.downloader.exception.DownloadException;

/**
 * Created by hrishikeshshinde on 29/11/16.
 */
public interface Downloader {

    DownloadState download(String source, String path, String fileName) throws DownloadException;
    DownloadState getStatus();
}
