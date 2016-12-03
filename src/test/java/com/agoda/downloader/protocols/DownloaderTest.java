package com.agoda.downloader.protocols;

import com.agoda.downloader.resources.FileResource;
import com.agoda.downloader.exceptions.DownloadException;
import com.agoda.downloader.DownloadSetup;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static com.agoda.downloader.protocols.DownloadState.COMPLETED;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by hrishikeshshinde on 27/11/16.
 */
public class DownloaderTest extends DownloadSetup {

    @Test
    public void fromHTTP() throws IOException, DownloadException {
        FileResource fileResource =  new FileResource(httpResource);
        String source = fileResource.getBaseURL();
        String fileName = fileResource.getFilename();

        Downloader downloader = Mockito.mock(HttpDownloader.class);
        when(downloader.download(source,downloadLocation,fileName)).thenReturn(DownloadState.COMPLETED);

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

        Downloader downloader = Mockito.mock(FtpDownloader.class);
        when(downloader.download(source,downloadLocation,fileName)).thenReturn(DownloadState.COMPLETED);

        DownloadState status = downloader.download(source, downloadLocation, fileName);
        assertEquals(COMPLETED, status);
    }
}
