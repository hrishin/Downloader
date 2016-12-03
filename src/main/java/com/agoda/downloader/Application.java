package com.agoda.downloader;

import com.agoda.downloader.exceptions.ConfigurationException;
import com.agoda.downloader.service.DownloadService;
import org.apache.commons.cli.ParseException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  Boots the application
 *  parse cli options using {@code {@link ConfigLoader}}
 *  schedules down sources using {@code {@link DownloadService}}
 *  Wait till all resources get processed
 */
public class Application {

	private final static Logger LOGGER = Logger.getLogger(Application.class.getName());
	private DownloadService downloadService;


	public static void main(String[] args) {
		ConfigLoader configLoader = null;
		String sources = null;
		String downloadLocation = null;

		try {
			configLoader = new ConfigLoader(args);
			sources = configLoader.getSources();
			downloadLocation = configLoader.getDownloadLocation();
		} catch (ParseException e) {
			LOGGER.log(Level.SEVERE, "Illegal arguments : " + e.getMessage());
			System.exit(0);
		} catch (ConfigurationException e) {
			LOGGER.log(Level.SEVERE, "Invalid configuration :" + e.getMessage());
			System.exit(0);
		}

		DownloadService downloadService = new DownloadService(sources,downloadLocation);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				downloadService.cleanActivities();
			}
		});
		downloadService.downloadAll();
		downloadService.waitForCompletion();
	}
}
