package com.agoda.downloader.domain;

/**
 * Created by hrishikeshshinde on 27/11/16.
 */

import java.net.MalformedURLException;

/**
 * FileResource is Unified File Resource for the context of application which supports
 * few other functionalities such as identify protocol and prepare files for
 * given URL
 */

public class FileResource {
    private final String baseURL;
    private final String protocol;

    public FileResource(String baseURL) throws MalformedURLException {
        this.baseURL = baseURL;
        this.protocol = protocol();
    }

    private String protocol() throws MalformedURLException {
        String protocol = this.baseURL.substring(0, this.baseURL.indexOf(":"));
        if(protocol == null ) {
            throw new MalformedURLException("Invalid protocol");
        }

        return protocol;
    }

    public String getBaseURL() {
        return baseURL;
    }

    @Override
    public String toString() {
        return "FileResource{" +
                "baseURL='" + baseURL + '\'' +
                ", protocol='" + protocol + '\'' +
                '}';
    }

    public String getProtocol() {
        return protocol;
    }
}
