package com.agoda.downloader.protocols;

/**
 * File sizing utility functions
 */
public class FileUtil {

    public static final int COMPLETE_RATIO = 100;

    private FileUtil() {}

    public static int calculateProgress(long bytesTransferred, long fileSize) {
        int progress = (int) ((bytesTransferred * COMPLETE_RATIO) / fileSize);
        return progress > COMPLETE_RATIO ? COMPLETE_RATIO : progress;
    }

    public static long inMB(long fileSize) {
        return inKB(fileSize) / 1024;
    }

    public static long inKB(long fileSize) {
        return fileSize/1024;
    }
}
