package com.agoda.downloader.service;

import com.agoda.downloader.domain.DownloadState;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.logging.Logger;

/**
 * Created by hrishikeshshinde on 29/11/16.
 */
public class HttpDownloader implements Downloader {
    private final static Logger LOGGER = Logger.getLogger(HttpDownloader.class.getName());

    private volatile DownloadState downloadState;

    public HttpDownloader() {
        downloadState = DownloadState.INITIAL;
    }

    @Override
    public DownloadState download(String source, String path, String fileName) throws IOException {
        this.downloadState = DownloadState.INPROGRESS;
        URL sourceUrl = new URL(source);
        HttpURLConnection connection = (HttpURLConnection) sourceUrl.openConnection();
        long fileSize = connection.getContentLengthLong();
        LOGGER.info("File size :" + fileSize + " bytes");
        LOGGER.info("File size :" + inMB(fileSize) + " MB");

        if(fileSize <= 0) {
            LOGGER.warning("Empty file");
            this.downloadState = DownloadState.FAILED;
            return this.downloadState;
        }

        InputStream downloadStream = connection.getInputStream();
        ReadableByteChannel readChannel = Channels.newChannel(downloadStream);

        File outputFile = new File(path + fileName);
        FileOutputStream fos = new FileOutputStream(outputFile);
        FileChannel writeChannel = fos.getChannel();

        long bytesTransfered = writeChannel.transferFrom(readChannel, 0, fileSize);

        fos.close();

        this.downloadState = (bytesTransfered == fileSize) ?
                        DownloadState.COMPLETED : DownloadState.FAILED;

        return this.downloadState;
    }

    private long inMB(long fileSize) {
        return inKB(fileSize) / 1024;
    }

    private long inKB(long fileSize) {
        return fileSize/1024;
    }


    @Override
    public DownloadState getStatus() {
        return downloadState;
    }
}
