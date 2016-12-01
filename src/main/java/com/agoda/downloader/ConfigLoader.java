package com.agoda.downloader;

import com.agoda.downloader.cliutil.CLIUtil;
import com.agoda.downloader.domain.CLIOptions;
import com.agoda.downloader.exception.ConfigurationException;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by hrishikeshshinde on 01/12/16.
 */
public class ConfigLoader {

    public ConfigLoader(String[] args) throws ParseException {
        CLIUtil.initOptions(args);
        CLIUtil.checkIfNeedHelp();
    }

    public String getDownloadLocation() throws ConfigurationException {
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

    public String getSources() {
        return  CLIUtil.getOptionValue(CLIOptions.DOWNLOAD_SOURCE_SS);
    }
}
