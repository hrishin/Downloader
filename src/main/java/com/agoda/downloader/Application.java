package com.agoda.downloader;

import com.agoda.downloader.exceptions.ConfigurationException;
import com.agoda.downloader.service.DownloadService;
import org.apache.commons.cli.ParseException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Application {

	private final static Logger LOGGER = Logger.getLogger(Application.class.getName());

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
		downloadService.downloadAll();
		downloadService.waitForCompletion();

	}
}
