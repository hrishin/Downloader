package com.agoda.downloader.resources;

/**
 * Created by hrishikeshshinde on 27/11/16.
 */

import com.agoda.downloader.protocols.Downloader;
import com.agoda.downloader.protocols.Protocol;
import com.agoda.downloader.protocols.ProtocolFactory;

import java.net.MalformedURLException;
import java.util.UUID;

/**
 * FileResource is File Resource for the context of application which supports
 * few other functionalities such as identify protocol and prepare file name for
 * given URL
 */

public class FileResource {
    private final String baseURL;
    private final String protocol;
    private final String filename;

    public FileResource(String baseURL) throws MalformedURLException {
        validate(baseURL);
        this.baseURL = baseURL;
        this.protocol = protocol();
        this.filename = fileName();
    }

    /**
     * Validate the URL format
     * @param baseURL
     */
    private void validate(String baseURL) {

    }

    private String fileName() {
        String fileName = buildFileName(this.baseURL);
        return fileName == null || fileName.trim().length() == 0 ? UUID.randomUUID().toString()
                                                                    :fileName;
    }

    /**
     * Constructs the filename by given URL
     * @param baseURL
     * @return
     */
    private String buildFileName(String baseURL) {
        return baseURL.substring(baseURL.lastIndexOf("/")+1, baseURL.length());
    }

    /**
     * Extracts the protocol of resources to use for downloading the resource
     * @return
     * @throws MalformedURLException
     */
    private String protocol() throws MalformedURLException {
        String protocol = this.baseURL.substring(0, this.baseURL.indexOf(":"));
        if(protocol == null || !Protocol.isSupported(protocol)) {
            throw new MalformedURLException("Invalid protocol");
        }

        return protocol.toLowerCase();
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

    public Downloader createDownloader() {
        return ProtocolFactory.getProtocol(protocol);
    }
}
