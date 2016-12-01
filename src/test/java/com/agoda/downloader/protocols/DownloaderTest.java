package com.agoda.downloader.protocols;

import com.agoda.downloader.domain.DownloadState;
import com.agoda.downloader.domain.FileResource;
import com.agoda.downloader.exception.DownloadException;
import com.agoda.downloader.DownloadSetup;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

import static com.agoda.downloader.domain.DownloadState.COMPLETED;
import static org.junit.Assert.assertEquals;

/**
 * Created by hrishikeshshinde on 27/11/16.
 */
public class DownloaderTest extends DownloadSetup {

    @Test
    public void fromHTTP() throws IOException, DownloadException {
        FileResource fileResource =  new FileResource(httpResource);
        String source = fileResource.getBaseURL();
        String fileName = fileResource.getFilename();

        Downloader downloader = new HttpDownloader();
        DownloadState status = downloader.download(source, downloadLocation, fileName);
        assertEquals(COMPLETED, status);
    }

    @Test(expected = DownloadException.class)
    public void emptyFileFromHTTP() throws IOException, DownloadException {
        String source = "https://s3.ap-south-1.amazonaws.com/hriships";
        FileResource fileResource =  new FileResource(source);
        source = fileResource.getBaseURL();
        String fileName = fileResource.getFilename();

        Downloader downloader = new HttpDownloader();
        downloader.download(source, downloadLocation, fileName);
    }

    @Test
    public void fromFTP() throws IOException, DownloadException {
        FileResource fileResource =  new FileResource(ftpResource);
        String source = fileResource.getBaseURL();
        String fileName = fileResource.getFilename();

        Downloader downloader = new FtpDownloader();
        DownloadState status = downloader.download(source, downloadLocation, fileName);
        assertEquals(COMPLETED, status);
    }
}
