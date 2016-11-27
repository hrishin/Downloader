package com.agoda.downloader;

import com.agoda.downloader.domain.FileResource;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by hrishikeshshinde on 27/11/16.
 */
public class FileResourceBuilderTest {
    @Test
    public void prepareFilesResources() {
        String sources = "http://my.file.com/file, ftp://other.file.com/other, sftp://and.also.this/ending";
        FileResourceBuilder resourceBuilder = new FileResourceBuilder();
        List<FileResource> fileList = resourceBuilder.frFromCSV(sources);

        assertEquals(3, fileList.size());
    }

    @Test
    public void validateResourceProtocol() {
        String sources = "http://my.file.com/file, https://my.file.com/file, ftp://other.file.com/other, sftp://and.also.this/ending";
        FileResourceBuilder resourceBuilder = new FileResourceBuilder();
        List<FileResource> fileList = resourceBuilder.frFromCSV(sources);

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