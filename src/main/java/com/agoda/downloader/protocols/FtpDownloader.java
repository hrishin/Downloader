package com.agoda.downloader.protocols;

import com.agoda.downloader.exceptions.DownloadException;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.io.CopyStreamAdapter;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by hrishikeshshinde on 30/11/16.
 */
public class FtpDownloader implements Downloader {

    private final static Logger LOGGER = Logger.getLogger(FtpDownloader.class.getName());

    public static final int DEFAULT_PORT = 21;

    private String server;
    private int port;
    private String filePath;
    private String userName;
    private String password;
    private volatile DownloadState downloadState;
    private long fileSize;

    public FtpDownloader() {
        this.downloadState = DownloadState.INITIAL;
    }

    @Override
    public DownloadState download(String source, String path, String fileName) throws DownloadException {
        String downloadFile = path + fileName;
        FTPClient ftpClient = null;

        try {
            processSourceURL(source);
            ftpClient = configuredFTPClient();
            verifyFile(path, fileName, ftpClient);
            downloadFile(path, fileName, ftpClient);
        } catch (IOException e) {
            this.downloadState = DownloadState.FAILED;
            throw new DownloadException(e, downloadFile);
        } finally {
            cleanUP(ftpClient);
        }

        return this.downloadState;
    }

    private void verifyFile(String path, String fileName, FTPClient ftpClient) throws IOException {
        FTPFile[] files = ftpClient.listFiles(this.filePath);
        this.fileSize = files[0].getSize();
        if(files.length <=  0 || this.fileSize <= 0) {
            throw new IOException("File is empty, could not download it");
        }
        LOGGER.info("File size :" + FileUtil.inKB(fileSize) + " bytes");
        LOGGER.info("File size :" + FileUtil.inMB(fileSize)+ " MB");
    }

    private void cleanUP(FTPClient ftpClient) {
        try {
            ftpClient.disconnect();
        } catch (IOException | NullPointerException e) {
            LOGGER.warning("FtpDownloader Stream close issue");
        }
    }

    private FTPClient configuredFTPClient() throws IOException {
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(this.server, this.port);
        ftpClient.enterLocalPassiveMode();
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        confgFTPCredentials(ftpClient);
        configureDownloadListener(ftpClient);
        return ftpClient;
    }

    private void configureDownloadListener(FTPClient ftpClient) {
        CopyStreamAdapter streamListener = new CopyStreamAdapter() {
            @Override
            public void bytesTransferred(long totalBytesTransferred, int bytesTransferred, long streamSize) {
                int percent = FileUtil.calculateProgress(totalBytesTransferred, fileSize);
                percent = percent > 100 ? 100 : percent;
                LOGGER.info("Download Status :" + filePath +" "+percent+"%");
            }

        };
        ftpClient.setCopyStreamListener(streamListener);

    }

    private void confgFTPCredentials(FTPClient ftpClient) throws IOException {
        if(userName != null && password != null) {
            ftpClient.login(this.userName, this.password);
        }
    }

    private void downloadFile(String path, String fileName, FTPClient ftpClient) throws IOException {
        LOGGER.log(Level.INFO, "FTP downloading : "+ fileName);
        File downloadFile = new File(path + fileName);
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(downloadFile);
            boolean success = ftpClient.retrieveFile(this.filePath, outputStream);
            if(success) {
                this.downloadState = DownloadState.COMPLETED;
            } else {
                throw new IOException("Failed to download the file");
            }
        } finally {
            cleanUp(outputStream);
        }
    }

    private void cleanUp(OutputStream outputStream) {
        if(outputStream != null) {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "FtpDownloader Stream close issue");
            }
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
        return this.downloadState;
    }
}
