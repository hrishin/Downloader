package com.agoda.downloader.protocols;

import com.agoda.downloader.exceptions.DownloadException;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.io.CopyStreamAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Downloads the resources using FTP protocol semantics
 */
public class FtpDownloader implements Downloader {

    private final static Logger LOGGER = Logger.getLogger(FtpDownloader.class.getName());

    public static final int DEFAULT_PORT = 21;
    public static final String EMPTY_FILE_ERR_MSG = "File is empty, could not download it";
    public static final String FILE_SIZE = "File size :";
    public static final String FTP_DOWNLOADER_STREAM_CLOSE_ISSUE = "FtpDownloader Stream close issue";
    public static final String DOWNLOAD_STATUS_MSG = "Download Status :";
    public static final String FTP_DOWNLOADING = "FTP downloading : ";
    public static final String DOWNLOAD_FAIL_ERR_MSG = "Failed to download the file";
    public static final String FILE_PATH_REGEX = "//";
    public static final String PATH_CREDENTIAL_REGEX = "@";
    public static final String PATH_REGEX = "/";

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

    /**
     * Download the requested resources using FTP protocol and save te file
     * on given path.
     * It also prints the progress of download process and update the download states
     *
     * Note: IF FTP resources is protected by username and password, make sure to pass the url using proper
     * URL semantics
     *
     * @param source
     * @param path
     * @param fileName
     * @return
     * @throws DownloadException
     */
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
            throw new IOException(EMPTY_FILE_ERR_MSG);
        }
        LOGGER.info(FILE_SIZE + FileUtil.inKB(fileSize) + " Bytes");
        LOGGER.info(FILE_SIZE + FileUtil.inMB(fileSize)+ " MB");
    }

    private void cleanUP(FTPClient ftpClient) {
        try {
            ftpClient.disconnect();
        } catch (IOException | NullPointerException e) {
            LOGGER.warning(FTP_DOWNLOADER_STREAM_CLOSE_ISSUE);
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
                LOGGER.info(DOWNLOAD_STATUS_MSG + filePath +" "+percent+"%");
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
        LOGGER.log(Level.INFO, FTP_DOWNLOADING + fileName);
        File downloadFile = new File(path + fileName);
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(downloadFile);
            boolean success = ftpClient.retrieveFile(this.filePath, outputStream);
            if(success) {
                this.downloadState = DownloadState.COMPLETED;
            } else {
                throw new IOException(DOWNLOAD_FAIL_ERR_MSG);
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
                LOGGER.log(Level.WARNING, FTP_DOWNLOADER_STREAM_CLOSE_ISSUE);
            }
        }
    }

    private void processSourceURL(String source) {
        if(source.contains("@")) {
            String[] sourceData = source.split(PATH_CREDENTIAL_REGEX);
            processCredentials(sourceData[0].split(FILE_PATH_REGEX)[1]);
            processPath(FILE_PATH_REGEX + sourceData[1]);
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
        String sourceData = pathData.split(FILE_PATH_REGEX)[1];
        if(sourceData.split(":").length == 2) {
            String[] hostPort = sourceData.split(":");
            this.port = Integer.parseInt(hostPort[1]);
            this.server = hostPort[0];
            this.filePath = sourceData.substring(sourceData.indexOf(PATH_REGEX), sourceData.length());
        } else {
            this.port = DEFAULT_PORT;
            this.server = sourceData.substring(0, sourceData.indexOf(PATH_REGEX));
            this.filePath = sourceData.substring(sourceData.indexOf(PATH_REGEX), sourceData.length());
        }
    }

    @Override
    public DownloadState getStatus() {
        return this.downloadState;
    }
}
