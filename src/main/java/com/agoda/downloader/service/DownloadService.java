package com.agoda.downloader.service;

import com.agoda.downloader.domain.FileResource;
import com.agoda.downloader.exception.DownloadException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Created by hrishikeshshinde on 01/12/16.
 */
public class DownloadService {
    private String sources;
    private String downloadLocation;
    private List<DownloadActivity> downloadActivities;
    private CompletionService<DownloadActivity> completionService;
    private List<Future<DownloadActivity>> activityFutures;

    public DownloadService(String sources, String downloadLocation) {
        this.sources = sources;
        this.downloadLocation = downloadLocation;
        this.downloadActivities = buildDownloadActivities(sources, downloadLocation);
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
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

    public void startDownloads() {
        for (DownloadActivity downloadActivty:downloadActivities) {
            Future<DownloadActivity> future = completionService.submit(downloadActivty.getDownloadCall());
            activityFutures.add(future);
        }
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
            throw new DownloadException(e, null);
        }
    }

    public List<DownloadActivity> getDownloadActivities() {
        return downloadActivities;
    }
}
