package com.agoda.downloader.service;

import com.agoda.downloader.domain.FileResource;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by hrishikeshshinde on 27/11/16.
 */
public class FileResourceBuilder {

    private static final Logger LOGGER = Logger.getLogger(FileResourceBuilder.class.getName());

    public List<FileResource> resourcesFromCSV(final String sources) {
        return Arrays.asList(sources.split(","))
                .stream()
                .map(url -> {
                    try {
                        return new FileResource(url.trim());
                    } catch (MalformedURLException e) {
                        LOGGER.warning("Invalid " +url + " : " + e.getMessage());
                        return null;
                    }
                })
                .filter(re -> re != null)
                .collect(Collectors.toList());
    }
}
