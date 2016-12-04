package com.agoda.downloader.protocols;

/**
 * Supported protocol list
 */
public enum Protocol {
    HTTP("http"),
    HTTPS("https"),
    FTP("ftp"),
    SFTP("sftp");

    private final transient Object value;

    private Protocol(final Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    public static boolean isSupported(String protocol) {
        for (Protocol proto : Protocol.values()) {
            if(proto.value.equals(protocol)) {
                return true;
            }
        }

        return false;
    }
}
