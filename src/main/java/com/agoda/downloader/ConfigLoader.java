package com.agoda.downloader;

import com.agoda.downloader.cliutil.CLIOptions;
import com.agoda.downloader.cliutil.CLIUtil;
import com.agoda.downloader.exceptions.ConfigurationException;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Loads the configuration for applications such as download location, input sources
 * as command line arguments
 */
public class ConfigLoader {

    /**
     * Initialize the command line options
     * Prompt for help if no commandline arguments has passed to program
     *
     * @param args
     * @throws ParseException
     */
    public ConfigLoader(String[] args) throws ParseException {
        CLIUtil.initOptions(args);
        if(args.length == 0 || hasValidInput()) {
            CLIUtil.help();
        }
    }

    private boolean hasValidInput() {
        return CLIUtil.getOptionValue(CLIOptions.DOWNLOAD_SOURCE_SS) == null ||
                CLIUtil.getOptionValue(CLIOptions.DOWNLOAD_LOCATION_SS) == null;
    }

    /**
     * Retrieves the download location for application and validate it as well by
     * checking file permissions, is it directory
     *
     * @return
     * @throws ConfigurationException
     */
    public String getDownloadLocation() throws ConfigurationException {
        CLIUtil.checkIfNeedHelp();
        String downloadLocation = CLIUtil.getOptionValue(CLIOptions.DOWNLOAD_LOCATION_SS);
        downloadLocation = downloadLocation.endsWith("/") ?
                        downloadLocation : downloadLocation+"/";
        validatePath(downloadLocation);
        return downloadLocation;
    }

    private void validatePath(String directoryPath) throws ConfigurationException {
        Path path = Paths.get(directoryPath);
        if(Files.exists(path)) {
            if(notDirAndWritable(path)) {
                throw new ConfigurationException(directoryPath + "is not not a directory or not writable");
            }
        } else {
            File directory = new File(directoryPath);
            if(!directory.mkdir()) {
                throw new ConfigurationException(directoryPath + "unable to create a directory");
            }
        }
    }

    private boolean notDirAndWritable(Path path) {
        return !Files.isDirectory(path) || !Files.isWritable(path);
    }

    /**
     * Retrieves resources CSV string passed to application
     * This string represent resources to download
     *
     * @return
     */
    public String getSources() {
        return  CLIUtil.getOptionValue(CLIOptions.DOWNLOAD_SOURCE_SS);
    }
}
