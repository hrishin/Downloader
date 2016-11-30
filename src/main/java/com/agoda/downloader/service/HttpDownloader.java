package com.agoda.downloader.service;

import com.agoda.downloader.domain.DownloadState;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

/**
 * Created by hrishikeshshinde on 29/11/16.
 */
public class HttpDownloader implements Downloader {

    private DownloadState downloadState;

    public HttpDownloader() {
        downloadState = DownloadState.INITIAL;
    }

    @Override
    public DownloadState download(String source, String path, String fileName) throws IOException {
        downloadState = DownloadState.INPROGRESS;
        URL sourceUrl = new URL(source);
        HttpURLConnection connection = (HttpURLConnection) sourceUrl.openConnection();
        long fileSize = connection.getContentLengthLong();

        InputStream is = connection.getInputStream();
        ReadableByteChannel rbc1 = Channels.newChannel(is);
        File outputFile = new File(path + fileName);
        FileOutputStream fos = new FileOutputStream(outputFile);

        FileChannel fileChannel1 = fos.getChannel();

        long bytesTransfered = fileChannel1.transferFrom(rbc1, 0, fileSize);

        fos.close();

        downloadState = (bytesTransfered == fileSize) ?
                        DownloadState.COMPLETED : DownloadState.FAILED;

        return downloadState;
    }

    @Override
    public DownloadState getStatus() {
        return downloadState;
    }
}
