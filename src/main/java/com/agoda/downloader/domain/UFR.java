package com.agoda.downloader.domain;

/**
 * Created by hrishikeshshinde on 27/11/16.
 */

/**
 * UFR is Unified File Resource for the context of application which supports
 * few other functionalities such as identify protocol and prepare files for
 * given URL
 */

public class UFR {
    private final String baseURL;

    public UFR(String baseURL) {
        this.baseURL = baseURL;
    }

    public String getBaseURL() {
        return baseURL;
    }

    @Override
    public String toString() {
        return "UFR{" +
                "baseURL='" + baseURL + '\'' +
                '}';
    }
}
