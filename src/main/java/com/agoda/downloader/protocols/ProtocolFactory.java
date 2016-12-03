package com.agoda.downloader.protocols;

/**
 * Created by hrishikeshshinde on 01/12/16.
 */
public class ProtocolFactory {

    /**
     * Build the corect instance of {@code {@link Downloader}}
     * based on {@code {@link Protocol}} passed
     *
     * @param protocol
     * @return
     */
    public static Downloader getProtocol(String protocol) {
        switch (Protocol.valueOf(protocol.toUpperCase())) {
            case HTTP:
            case HTTPS:
                return new HttpDownloader();

            case FTP:
            case SFTP:
                return new FtpDownloader();

            default:
                return null;
        }
    }
}
