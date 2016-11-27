package com.agoda.downloader;

import com.agoda.downloader.domain.UFR;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by hrishikeshshinde on 27/11/16.
 */
public class UFRBuilder {

    public List<UFR> urfFromCSV(final String sources) {
        return Arrays.asList(sources.split(","))
                .stream()
                .map(url -> new UFR(url.trim()))
                .collect(Collectors.toList());
    }
}
