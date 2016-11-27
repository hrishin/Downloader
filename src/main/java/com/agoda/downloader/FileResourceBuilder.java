package com.agoda.downloader;

import com.agoda.downloader.domain.FileResource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by hrishikeshshinde on 27/11/16.
 */
public class FileResourceBuilder {

    public List<FileResource> frFromCSV(final String sources) {
        return Arrays.asList(sources.split(","))
                .stream()
                .map(url -> new FileResource(url.trim()))
                .collect(Collectors.toList());
    }
}
