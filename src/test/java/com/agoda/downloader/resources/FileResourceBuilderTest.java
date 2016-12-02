package com.agoda.downloader.resources;

import com.agoda.downloader.resources.FileResource;
import com.agoda.downloader.resources.FileResourceBuilder;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by hrishikeshshinde on 27/11/16.
 */
public class FileResourceBuilderTest {
    @Test
    public void prepareFileResources() {
        String sources = "http://my.file.com/file, ftp://other.file.com/other, sftp://and.also.this/ending";
        FileResourceBuilder resourceBuilder = new FileResourceBuilder();
        List<FileResource> fileList = resourceBuilder.resourcesFromCSV(sources);

        assertEquals(3, fileList.size());
    }

    @Test
    public void validateResourceProtocol() {
        String sources = "http://my.file.com/file, https://my.file.com/file, ftp://other.file.com/other, sftp://and.also.this/ending";
        FileResourceBuilder resourceBuilder = new FileResourceBuilder();
        List<FileResource> fileList = resourceBuilder.resourcesFromCSV(sources);

        fileList.forEach(resource -> {
            assertTrue(havingValidProtocol(resource));
        });
    }

    private boolean havingValidProtocol(FileResource resource) {
        return resource.getProtocol().equals("http") ||
                resource.getProtocol().equals("https") ||
                resource.getProtocol().equals("ftp") ||
                resource.getProtocol().equals("sftp");
    }
}