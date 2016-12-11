package com.agoda.downloader.service;

import com.agoda.downloader.exceptions.DownloadException;
import com.agoda.downloader.resources.FileResource;
import com.agoda.downloader.resources.FileResourceBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Is service component of application which manage all {@code {@link DownloadActivity}}.
 * It schedules all download request concurrently using thread pool
 *
 */
public class DownloadService {
    private final static Logger LOGGER = Logger.getLogger(DownloadService.class.getName());
    public static final String DOWNLOAD_COMPLETE = "Download Complete : ";
    public static final String DOWNLOAD_FAILED = "Download Failed : ";

    private final String sources;
    private final String downloadLocation;
    private final List<DownloadActivity> downloadActivities;
    private final CompletionService<DownloadActivity> completionService;
    private final List<Future<DownloadActivity>> activityFutures;
    private final ExecutorService executor;

    public DownloadService(String sources, String downloadLocation) {
        this.sources = sources;
        this.downloadLocation = downloadLocation;
        this.downloadActivities = buildDownloadActivities(sources, downloadLocation);
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        this.completionService = new ExecutorCompletionService<DownloadActivity>(executor);
        this.activityFutures =  new ArrayList<Future<DownloadActivity>>();
    }

    private List<DownloadActivity> buildDownloadActivities(String sources, String downloadLocation) {
        List<FileResource> resources = new FileResourceBuilder()
                .resourcesFromCSV(sources);

        return resources.stream()
                .map(resource -> new DownloadActivity(resource, downloadLocation))
                .collect(Collectors.toList());
    }

    /**
     * Starts downloading all requested resources by submitting {@code {@link DownloadActivity}}
     * for each resources to thread scheduler
     */
    public void downloadAll() {
        for (DownloadActivity downloadActivty : downloadActivities) {
            Future<DownloadActivity> future = completionService.submit(downloadActivty.getDownloadCall());
            activityFutures.add(future);
        }
        this.executor.shutdown();
    }

    /**
     * Retrieves the recently finished or updated {@code {@link DownloadActivity}}. Caller get blocked until
     * activity get available
     * It allows service consumer to retrieve one by one activity as they its download process
     * get finished without waiting for all download request to complete
     *
     * @return
     * @throws DownloadException
     */
    public DownloadActivity getUpdatedActivity() throws DownloadException {
        try {
            return completionService.take().get();
        } catch (InterruptedException | ExecutionException e) {
            if(e instanceof DownloadException)
                throw new DownloadException(e, ((DownloadException) e).getFilePath());
            else
                throw new DownloadException(e, null);
        }
    }

    /**
     *  Get all {@code {@link DownloadActivity}} that represent Future task
     * @return
     */
    public List<DownloadActivity> getDownloadActivities() {
        return downloadActivities.stream().collect(Collectors.toList());
    }

    /**
     * Allows service consumer to wait until all download task get finish
     * It also logs the status of each activity as well
     *
     */
    public void waitForCompletion() {
        for (int i=0; i < downloadActivities.size(); i++) {
            try {
                DownloadActivity activity = getUpdatedActivity();
                LOGGER.log(Level.INFO, DOWNLOAD_COMPLETE + activity.getFileName());
            } catch (DownloadException e) {
                LOGGER.log(Level.INFO, DOWNLOAD_FAILED + e.getFilePath());
            }
        }
    }


    /**
     * Clean up all activities that are pending, incomplete or never initiated.
     * this method is useful while executing cleanup service during application shutdown
     */
    public void cleanActivities() {
        downloadActivities.stream().forEach(act -> act.clean());
    }
}
