package com.agoda.downloader.domain;

/**
 * Created by hrishikeshshinde on 27/11/16.
 */

import org.mockito.internal.matchers.Null;

import java.net.MalformedURLException;
import java.util.UUID;

/**
 * FileResource is Unified File Resource for the context of application which supports
 * few other functionalities such as identify protocol and prepare file name for
 * given URL
 */

public class FileResource {
    private final String baseURL;
    private final String protocol;
    private final String filename;

    public FileResource(String baseURL) throws MalformedURLException {
        this.baseURL = baseURL;
        this.protocol = protocol();
        this.filename = fileName();
    }

    private String fileName() {
        String baseURL = this.baseURL;
        String fileName = baseURL.substring(baseURL.lastIndexOf("/")+1, baseURL.length());
        String uid = UUID.randomUUID().toString();
        return fileName != null ? uid.substring(uid.lastIndexOf("-"), uid.length()) + fileName : uid;
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
                ", filename='" + filename + '\'' +
                '}';
    }

    public String getProtocol() {
        return protocol;
    }

    public String getFilename() {
        return filename;
    }

    public boolean download(String path) {
        return false;
    }
}
