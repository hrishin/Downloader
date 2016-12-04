package com.agoda.downloader.protocols;

/**
 * File sizing utility functions
 */
public class FileUtil {

    private FileUtil() {}

    public static int calculateProgress(long bytesTransferred, long fileSize) {
        int progress = (int) ((bytesTransferred * 100) / fileSize);
        return progress > 100 ? 100 : progress;
    }

    public static long inMB(long fileSize) {
        return inKB(fileSize) / 1024;
    }

    public static long inKB(long fileSize) {
        return fileSize/1024;
    }
}
