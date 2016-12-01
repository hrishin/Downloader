package com.agoda.downloader.service;

import com.agoda.downloader.domain.DownloadState;
import com.agoda.downloader.exception.DownloadException;

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
    public DownloadState download(String source, String path, String fileName) throws DownloadException {
        this.downloadState = DownloadState.INPROGRESS;
        String downloadFile = path + fileName;
        InputStream downloadStream = null;
        FileOutputStream fos = null;


        try {
            URL sourceUrl = new URL(source);
            HttpURLConnection connection = (HttpURLConnection) sourceUrl.openConnection();
            long fileSize = connection.getContentLengthLong();
            LOGGER.info("File size :" + fileSize + " bytes");
            LOGGER.info("File size :" + inMB(fileSize) + " MB");

            if(fileSize <= 0) {
                throw new IOException("File is empty, could not download it");
            }

            downloadStream = connection.getInputStream();
            ReadableByteChannel readChannel = Channels.newChannel(downloadStream);

            File outputFile = new File(downloadFile);
            fos = new FileOutputStream(outputFile);
            FileChannel writeChannel = fos.getChannel();

            long bytesTransferred = writeChannel.transferFrom(readChannel, 0, fileSize);

            fos.close();

            if(bytesTransferred != fileSize) {
                throw new IOException("Failed to download the file");
            } else {
                this.downloadState = DownloadState.COMPLETED;
            }

        } catch (IOException e) {
            this.downloadState = DownloadState.FAILED;
            throw new DownloadException(e, downloadFile);
        } finally {
            cleanUpStreams(downloadStream, fos);
        }

        return this.downloadState;
    }

    private void cleanUpStreams(InputStream downloadStream, FileOutputStream fos) {
        flushAndCloseStream(fos);
        closeStream(downloadStream);
    }

    private void closeStream(InputStream downloadStream) {
        if(downloadStream != null) {
            try {
                downloadStream.close();
            } catch (IOException e) {
                LOGGER.warning("HttpDownloader Stream close issue");
            }
        }
    }

    private void flushAndCloseStream(FileOutputStream fos) {
        if(fos != null) {
            try {
                fos.flush();
                fos.close();
            } catch (IOException e) {
                LOGGER.warning("HttpDownloader Stream close issue");
            }
        }
    }

    private long inMB(long fileSize) {
        return inKB(fileSize) / 1024;
    }

    private long inKB(long fileSize) {
        return fileSize/1024;
    }

    @Override
    public DownloadState getStatus() {
        return this.downloadState;
    }
}
