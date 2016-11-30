package com.agoda.downloader.service;

import com.agoda.downloader.domain.FileResource;
import com.agoda.downloader.domain.DownloadState;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;

import static org.junit.Assert.assertTrue;

/**
 * Created by hrishikeshshinde on 27/11/16.
 */
public class DownloaderTest {

    @Test
    public void fromHTTP() throws IOException {
        String source = "https://s3.ap-south-1.amazonaws.com/hriships/HrishikeshShinde_resume.pdf";
        String path = "/Users/hrishikeshshinde/";
        FileResource fileResource =  new FileResource(source);
        String fileName = fileResource.getFilename();

        Downloader downloader = new HttpDownloader();
        DownloadState status = downloader.download(source, path, fileName);
        assertTrue(status == DownloadState.COMPLETED);
    }

    @Test
    public void emptyFileFromHTTP() throws IOException {
        String source = "https://s3.ap-south-1.amazonaws.com/hriships";
        String path = "/Users/hrishikeshshinde/";
        FileResource fileResource =  new FileResource(source);
        String fileName = fileResource.getFilename();

        Downloader downloader = new HttpDownloader();
        DownloadState status = downloader.download(source, path, fileName);
        assertTrue(status == DownloadState.FAILED);
    }

    @Test
    @Ignore
    public void fromFTP() throws IOException {
        String source = "ftp://ftp.hq.nasa.gov/pub/astrophysics/FTP_Instructions.txt";
        String path = "/Users/hrishikeshshinde/";
        FileResource fileResource =  new FileResource(source);
        String fileName = fileResource.getFilename();

        Downloader downloader = new HttpDownloader();
        DownloadState status = downloader.download(source, path, fileName);
        assertTrue(status == DownloadState.COMPLETED);
    }
}
