package com.agoda.downloader.resources;

import com.agoda.downloader.DownloadSetup;
import com.agoda.downloader.protocols.Protocol;
import com.agoda.downloader.resources.FileResource;
import com.agoda.downloader.resources.FileResourceBuilder;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by hrishikeshshinde on 27/11/16.
 */
public class FileResourceBuilderTest extends DownloadSetup {
    @Test
    public void prepareFileResources() {
        String sources = "http://my.file.com/file, ftp://other.file.com/other, sftp://and.also.this/ending";
        FileResourceBuilder resourceBuilder = new FileResourceBuilder();
        List<FileResource> fileList = resourceBuilder.resourcesFromCSV(sources);

        assertEquals(3, fileList.size());
    }

    @Test
    public void checkInvalidURLS() {
        String sources = "http://, asdasdasdsa, /##/";
        FileResourceBuilder resourceBuilder = new FileResourceBuilder();
        List<FileResource> fileList = resourceBuilder.resourcesFromCSV(sources);

        assertEquals(1, fileList.size());
    }
}