package com.agoda.downloader;

import com.agoda.downloader.exceptions.ConfigurationException;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by hrishikeshshinde on 01/12/16.
 */
public class ConfigLoderTest {

    @Test
    public void loadConfiguration() throws ParseException, ConfigurationException {
        String[] args = {"-dl", "/Users/hrishikeshshinde/downloadtest/",
                        "-s", "http://my.file.com/file, ftp://other.file.com/other, sftp://and.also.this/ending"};

        ConfigLoader configLoader = new ConfigLoader(args);
        String downloadLocation = configLoader.getDownloadLocation();
        String downloadSources = configLoader.getSources();

        assertTrue(downloadLocation != null && downloadLocation.endsWith("/")
                   && downloadSources != null);
    }

    @Test(expected = ConfigurationException.class)
    public void invalidDownloadPath() throws ParseException, ConfigurationException {
        String[] args = {"-dl", "U/downloadtest/",
                "-s", "http://my.file.com/file, ftp://other.file.com/other, sftp://and.also.this/ending"};

        ConfigLoader configLoader = new ConfigLoader(args);
        configLoader.getDownloadLocation();
    }

    @Test(expected = ConfigurationException.class)
    public void isNotWritable() throws ParseException, ConfigurationException {
        String[] args = {"-dl", "/bin",
                "-s", "http://my.file.com/file, ftp://other.file.com/other, sftp://and.also.this/ending"};

        ConfigLoader configLoader = new ConfigLoader(args);
        configLoader.getDownloadLocation();
    }
}