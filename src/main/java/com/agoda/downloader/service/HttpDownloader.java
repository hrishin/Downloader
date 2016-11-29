package com.agoda.downloader.service;

import com.agoda.downloader.domain.FileResource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

/**
 * Created by hrishikeshshinde on 29/11/16.
 */
public class HttpDownloader implements Downloader {

    @Override
    public boolean download(String source, String path, String fileName) throws IOException {
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

        return bytesTransfered == fileSize;
    }
}
