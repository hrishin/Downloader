package com.agoda.downloader.domain;

/**
 * Created by hrishikeshshinde on 01/12/16.
 */
public enum OPTION_NAME {
    DOWNLOAD_LOCATION_SS("dl"),
    DOWNLOAD_LOCATION_LS("dlpath"),
    DOWNLOAD_SOURCE_SS("s"),
    DOWNLOAD_SOURCE_LS("source"),
    HELP_SS("h"),
    HELP_LS("help");

    private final transient Object value;

    private OPTION_NAME(final Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
