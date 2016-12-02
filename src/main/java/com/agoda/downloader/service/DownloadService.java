package com.agoda.downloader.service;

import com.agoda.downloader.Application;
import com.agoda.downloader.resources.FileResource;
import com.agoda.downloader.exceptions.DownloadException;
import com.agoda.downloader.resources.FileResourceBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by hrishikeshshinde on 01/12/16.
 */
public class DownloadService {
    private final static Logger LOGGER = Logger.getLogger(DownloadService.class.getName());

    private String sources;
    private String downloadLocation;
    private List<DownloadActivity> downloadActivities;
    private CompletionService<DownloadActivity> completionService;
    private List<Future<DownloadActivity>> activityFutures;
    private ExecutorService executor;

    public DownloadService(String sources, String downloadLocation) {
        this.sources = sources;
        this.downloadLocation = downloadLocation;
        this.downloadActivities = buildDownloadActivities(sources, downloadLocation);
        this.executor = Executors.newCachedThreadPool();
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

    public void downloadAll() {
        for (DownloadActivity downloadActivty:downloadActivities) {
            Future<DownloadActivity> future = completionService.submit(downloadActivty.getDownloadCall());
            activityFutures.add(future);
        }
        this.executor.shutdown();
    }

    public void getActivityStats() {
        for (Future futureActivity: activityFutures) {
            futureActivity.isDone();
        }
    }

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

    public List<DownloadActivity> getDownloadActivities() {
        return downloadActivities;
    }

    public void waitForCompletion() {
        for (int i=0; i < downloadActivities.size(); i++) {
            try {
                DownloadActivity activity = getUpdatedActivity();
                LOGGER.log(Level.INFO, "Download Complete : " + activity.getFileName());
            } catch (DownloadException e) {
                LOGGER.log(Level.INFO, "Download Failed : " + e.getFilePath());
            }

        }
    }
}
