package com.agoda.downloader.protocols;

import com.agoda.downloader.domain.DOWNLOAD_STATE;
import com.agoda.downloader.exception.DownloadException;

/**
 * Created by hrishikeshshinde on 29/11/16.
 */
public interface Downloader {

    DOWNLOAD_STATE download(String source, String path, String fileName) throws DownloadException;
    DOWNLOAD_STATE getStatus();
}
