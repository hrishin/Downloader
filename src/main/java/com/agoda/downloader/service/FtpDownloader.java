package com.agoda.downloader.service;

import com.agoda.downloader.domain.DownloadState;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.*;

/**
 * Created by hrishikeshshinde on 30/11/16.
 */
public class FtpDownloader implements Downloader {

    public static final int DEFAULT_PORT = 21;

    private String server;
    private int port;
    private String filePath;
    private String userName;
    private String password;
    private volatile DownloadState downloadState;

    public FtpDownloader() {
        this.downloadState = DownloadState.INITIAL;
    }

    @Override
    public DownloadState download(String source, String path, String fileName) throws IOException {
        processSourceURL(source);
        FTPClient ftpClient = configuredFTPClient();

        FTPFile[] files = ftpClient.listFiles(filePath);
        if(files.length <=  0) {
            this.downloadState = DownloadState.FAILED;
        } else {
            downloadFile(path, fileName, ftpClient);
        }

        return this.downloadState;
    }

    private void downloadFile(String path, String fileName, FTPClient ftpClient) throws IOException {
        File downloadFile = new File(path + fileName);
        try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadFile))) {
            boolean success = ftpClient.retrieveFile(this.filePath, outputStream);
            if(success) {
                this.downloadState = DownloadState.COMPLETED;
            } else {
                this.downloadState = DownloadState.FAILED;
            }
        }
    }

    private FTPClient configuredFTPClient() throws IOException {
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(this.server, this.port);
        ftpClient.enterLocalPassiveMode();
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        confgFTPCredentials(ftpClient);
        return ftpClient;
    }

    private void confgFTPCredentials(FTPClient ftpClient) throws IOException {
        if(userName != null && password != null) {
            ftpClient.login(this.userName, this.password);
        }
    }

    private void processSourceURL(String source) {
        if(source.contains("@")) {
            String[] sourceData = source.split("@");
            processCredentials(sourceData[0].split("//")[1]);
            processPath("//" + sourceData[1]);
        } else {
            processCredentials(source);
        }
    }

    private void processCredentials(String userdata) {
        String[] credentials = userdata.split(":");
        this.userName = credentials[0];
        this.password = credentials[1];
    }

    private void processPath(String pathData) {
        String sourceData = pathData.split("//")[1];
        if(sourceData.split(":").length == 2) {
            String[] hostPort = sourceData.split(":");
            this.port = Integer.parseInt(hostPort[1]);
            this.server = hostPort[0];
            this.filePath = sourceData.substring(sourceData.indexOf("/"), sourceData.length());
        } else {
            this.port = DEFAULT_PORT;
            this.server = sourceData.substring(0, sourceData.indexOf("/"));
            this.filePath = sourceData.substring(sourceData.indexOf("/"), sourceData.length());
        }
    }

    @Override
    public DownloadState getStatus() {
        return null;
    }
}
