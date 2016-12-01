package com.agoda.downloader.exception;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 * Created by hrishikeshshinde on 30/11/16.
 */
public class DownloadException extends Exception {
    private final static Logger LOGGER = Logger.getLogger(DownloadException.class.getName());

    final String filePath;

    public DownloadException(String filePath) {
        this.filePath = filePath;
        cleanUp(filePath);
    }

    public DownloadException(String message, String filePath) {
        super(message);
        this.filePath = filePath;
        cleanUp(filePath);
    }

    public DownloadException(String message, Throwable cause, String filePath) {
        super(message, cause);
        this.filePath = filePath;
        cleanUp(filePath);
    }

    public DownloadException(Throwable cause, String filePath) {
        super(cause);
        this.filePath = filePath;
        cleanUp(filePath);
    }

    public DownloadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String filePath) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.filePath = filePath;
        cleanUp(filePath);
    }

    private void cleanUp(String filePath) {
        try {
            Files.delete(Paths.get(filePath));
            LOGGER.info("File cleaned : " + filePath);
        } catch (Exception e) {
            LOGGER.info("File cleaning failed :" + filePath);
        }
    }

    public String getFilePath() {
        return filePath;
    }
}
