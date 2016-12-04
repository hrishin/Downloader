package com.agoda.downloader.protocols;

import com.agoda.downloader.exceptions.DownloadException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

/**
 * Downloads the resources using HTTP protocol semantics
 */
public class HttpDownloader implements Downloader {
    private final static Logger LOGGER = Logger.getLogger(HttpDownloader.class.getName());

    private String filePath;
    private long fileSize;
    private int lastProgress;
    private volatile DownloadState downloadState;

    public HttpDownloader() {
        downloadState = DownloadState.INITIAL;
        lastProgress = -1;
    }

    /**
     * Download the requested resources using HTTP protocol and save te file
     * on given path
     * It also prints the progress of download process and update the download states
     *
     * @param source
     * @param path
     * @param fileName
     * @return
     * @throws DownloadException
     */
    @Override
    public DownloadState download(String source, String path, String fileName) throws DownloadException {
        InputStream downloadStream = null;
        FileOutputStream fileOPStream = null;
        downloadState = DownloadState.INPROGRESS;
        filePath = path + fileName;

        try {
            HttpURLConnection connection = configureStreams(source);
            verifyFile(connection);

            downloadStream = connection.getInputStream();
            File outputFile = new File(filePath);
            fileOPStream = new FileOutputStream(outputFile);

            downloadFile(downloadStream, fileOPStream);

        } catch (IOException e) {
            this.downloadState = DownloadState.FAILED;
            throw new DownloadException(e, filePath);
        } finally {
            cleanUpStreams(downloadStream, fileOPStream);
        }

        return this.downloadState;
    }

    private void downloadFile(InputStream downloadStream, FileOutputStream fileOPStream) throws IOException {
        byte[] buf=new byte[8192];
        int bytesRead = 0, bytesBuffered = 0;
        long bytesTransferred = 0;
        while((bytesRead = downloadStream.read(buf)) > -1 ) {
            fileOPStream.write(buf, 0, bytesRead);
            bytesBuffered += bytesRead;
            bytesBuffered = flushAccumulatedBuffer(fileOPStream, bytesBuffered);
            bytesTransferred += bytesRead;
            calculateProgress(bytesTransferred);
        }

        fileOPStream.flush();

        if(bytesTransferred != fileSize) {
            throw new IOException("Failed to download the file");
        } else {
            this.downloadState = DownloadState.COMPLETED;
        }
    }

    private void calculateProgress(long bytesTransferred) {
        int progress = FileUtil.calculateProgress(bytesTransferred, fileSize);
        if(progress != lastProgress) {
            LOGGER.info("Download Status : " + filePath +" "+ progress +"%");
            lastProgress = progress;
        }
    }

    private int flushAccumulatedBuffer(FileOutputStream fileOPStream, int bytesBuffered) throws IOException {
        if (bytesBuffered > 1024 * 1024) {
            bytesBuffered = 0;
            fileOPStream.flush();
        }
        return bytesBuffered;
    }

    private void verifyFile(HttpURLConnection connection) throws IOException {
        fileSize = connection.getContentLengthLong();
        LOGGER.info("File size :" + FileUtil.inKB(fileSize) + " bytes");
        LOGGER.info("File size :" + FileUtil.inMB(fileSize)+ " MB");

        if(fileSize <= 0) {
            throw new IOException("File is empty, could not download it");
        }
    }

    private HttpURLConnection configureStreams(String source) throws IOException {
        URL sourceUrl = new URL(source);
        return (HttpURLConnection) sourceUrl.openConnection();
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

    /**
     * Gives current state of download process
     *
     * @return
     */
    @Override
    public DownloadState getStatus() {
        return this.downloadState;
    }
}
