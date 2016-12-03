package com.agoda.downloader.service;

import com.agoda.downloader.protocols.DownloadState;
import com.agoda.downloader.protocols.HttpDownloader;
import com.agoda.downloader.resources.FileResource;
import com.agoda.downloader.protocols.Downloader;
import org.apache.commons.io.FileDeleteStrategy;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by hrishikeshshinde on 01/12/16.
 *
 * Objective of the class is to manage operations and states of individual download activity in application.
 * To manage downloading aspects it uses {@code {@link FileResource}} and {@code {@link Downloader}} objects.
 * It could allow to add new features like pause pause/resume download
 */

public class DownloadActivity {
    private final static Logger LOGGER = Logger.getLogger(DownloadActivity.class.getName());

    private final FileResource fileResource;
    private final Downloader downloader;
    private final String downloadPath;

    public DownloadActivity(FileResource fileResource, String downloadPath) {
        this.fileResource = fileResource;
        this.downloader = this.fileResource.createDownloader();
        this.downloadPath = downloadPath;
    }

    public DownloadActivity(FileResource fileResource, Downloader downloader, String downloadPath) {
        this.fileResource = fileResource;
        this.downloader = downloader;
        this.downloadPath = downloadPath;
    }

    /**
     * Returns {@code {@link Callable}} instances of {@code {@link DownloadTask}}
     * @return
     */
    public Callable<DownloadActivity> getDownloadCall() {
        return new DownloadTask();
    }

    /**
     * Gives current state of downloading activity as {@code {@link DownloadState}}
     * using {@code {@link Downloader}}
     * @return
     */
    public DownloadState getStatus() {
        return this.downloader.getStatus();
    }

    /**
     * Get the filename of resource to download
     * @return
     */
    public String getFileName() {
        return fileResource.getFilename();
    }

    /**
     * It removes the file from disk it activity state is incomplete or partial
     */
    public void clean() {
        if(!downloader.getStatus().equals(DownloadState.COMPLETED)) {
            String filePath = downloadPath + fileResource.getFilename();
            try {
                FileDeleteStrategy.FORCE.delete(new File(filePath));
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "cant cleanup " + filePath );
            }
        }
    }

    /**
     * Creates new Callable DownloadTask and override call method to start
     * resources downloading using {@code {@link Downloader}} instance
     */
    class DownloadTask implements Callable<DownloadActivity> {
        private DownloadActivity activity;

        DownloadTask() {
            activity = DownloadActivity.this;
        }

        @Override
        public DownloadActivity call() throws Exception {
            downloader.download(fileResource.getBaseURL(), downloadPath, fileResource.getFilename());
            return activity;
        }
    }
}
