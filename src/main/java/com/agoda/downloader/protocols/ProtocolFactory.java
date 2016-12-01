package com.agoda.downloader.protocols;

/**
 * Created by hrishikeshshinde on 01/12/16.
 */
public class ProtocolFactory {

    public static Downloader getProtocol(String protocol) {
        switch (protocol) {
            case "http":
            case "https":
                return new HttpDownloader();

            case "ftp":
            case "sftp":
                return new FtpDownloader();

            default:
                return null;
        }
    }
}
