package com.agoda.downloader.resources;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Builds FileResource entities from CSV string
 */
public class FileResourceBuilder {

    private static final Logger LOGGER = Logger.getLogger(FileResourceBuilder.class.getName());
    public static final String INVALID_URL = "Invalid ";

    /**
     * Build the list of {@code {@link FileResource}}
     * by parsing CSV of download resource string
     *
     * @param sources
     * @return
     */
    public List<FileResource> resourcesFromCSV(final String sources) {
        return Arrays.asList(sources.split(","))
                .stream()
                .map(url -> {
                    try {
                        return new FileResource(url.trim());
                    } catch (MalformedURLException e) {
                        LOGGER.log(Level.WARNING, INVALID_URL + url + " : " + e.getMessage());
                        return null;
                    }
                })
                .filter(re -> re != null)
                .collect(Collectors.toList());
    }
}
