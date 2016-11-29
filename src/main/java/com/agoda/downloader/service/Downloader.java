package com.agoda.downloader.service;

import com.agoda.downloader.domain.FileResource;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created by hrishikeshshinde on 29/11/16.
 */
public interface Downloader {


    boolean download(String source, String path, String fileName) throws IOException;
}
