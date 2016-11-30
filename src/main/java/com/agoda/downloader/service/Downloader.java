package com.agoda.downloader.service;

import com.agoda.downloader.domain.DownloadState;

import java.io.IOException;

/**
 * Created by hrishikeshshinde on 29/11/16.
 */
public interface Downloader {


    DownloadState download(String source, String path, String fileName) throws IOException;
    DownloadState getStatus();
}
