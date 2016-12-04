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
 * few other functionalities such as identify extractProtocol and prepare file name for
 * given URL
 */

public class FileResource {
    private final String baseURL;
    private final String protocol;
    private final String filename;

    public FileResource(String baseURL) throws MalformedURLException {
        this.baseURL = baseURL;
        this.protocol = extractProtocol();
        this.filename = generateFileName();
    }

    /**
     * Extracts the extractProtocol of resources to use for downloading the resource
     * @return
     * @throws MalformedURLException
     */
    private String extractProtocol() throws MalformedURLException {
        String protocol = extractProtocolString();
        if(protocol == null || !Protocol.isSupported(protocol)) {
            throw new MalformedURLException("Invalid protocol or no protocol support for URL : " + baseURL);
        }

        return protocol.toLowerCase();
    }

    private String extractProtocolString() {
        try {
            return this.baseURL.substring(0, this.baseURL.indexOf(":"));
        } catch (Exception e) {
            return null;
        }
    }

    private String generateFileName() {
        String fileName = buildFileName();
        return checkIfNull(fileName);
    }

    private String buildFileName() {
        return baseURL.substring(baseURL.lastIndexOf("/")+1, baseURL.length());
    }

    private String checkIfNull(String fileName) {
        return fileNameIsNull(fileName) ? UUID.randomUUID().toString(): getUniqueFileName(fileName);
    }

    private String getUniqueFileName(String fileName) {
        return Math.abs(baseURL.hashCode())+"_"+fileName;
    }

    private boolean fileNameIsNull(String fileName) {
        return fileName == null || fileName.trim().length() == 0;
    }

    /**
     * Gives origin URL of download resource
     *
     * @return
     */
    public String getBaseURL() {
        return baseURL;
    }

    @Override
    public String toString() {
        return "FileResource{" +
                "baseURL='" + baseURL + '\'' +
                ", extractProtocol='" + protocol + '\'' +
                ", filename='" + filename + '\'' +
                '}';
    }

    /**
     * Gives {@code {@link Protocol}} to used for download resource
     *
     * @return
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * gives file name of download resource
     * @return
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Create correct instance of {@code {@link Downloader}}
     * based on protocol used for resource using {@code {@link ProtocolFactory}}
     *
     * @return
     */
    public Downloader createDownloader() {
        return ProtocolFactory.getProtocol(protocol);
    }

    public void delete() {
    }
}
