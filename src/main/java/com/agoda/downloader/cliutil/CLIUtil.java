package com.agoda.downloader.cliutil;

import org.apache.commons.cli.*;

/**
 * Utility class to initialize, extract CLI options
 */
public class CLIUtil {
    private static CommandLineParser parser;
    private static CommandLine commandLine;
    private static Options options;

    /**
     * Build the command line options for the applciation
     *
     * @param args
     * @throws ParseException
     */
    public static void initOptions(String[] args) throws ParseException {
        Option firstNDataOption = new Option(CLIOptions.DOWNLOAD_LOCATION_SS.toString(), CLIOptions.DOWNLOAD_LOCATION_LS.toString(),
                                            true, "Local download path");
        Option inputDataOption  = new Option(CLIOptions.DOWNLOAD_SOURCE_SS.toString(),
                                            CLIOptions.DOWNLOAD_SOURCE_LS.toString(), true, "Downlaod data sources in csv form");
        Option helpOption 		= new Option(CLIOptions.HELP_SS.toString(), "Help");

        options = new Options();
        options.addOption(firstNDataOption);
        options.addOption(inputDataOption);
        options.addOption(helpOption);

        parser = new PosixParser();
        commandLine = parser.parse(options, args);

    }

    /**
     *Get the value of option passed as command line argument
     *
     * @param switchName
     * @return
     */
    public static String getOptionValue(CLIOptions switchName) {
        if (commandLine.hasOption(switchName.toString())) {
            return commandLine.getOptionValue(switchName.toString());
        } else {
            return null;
        }
    }

    /**
     * Prompts for help option if passed as commandline argument
     *
     */
    public static void checkIfNeedHelp() {
        if(commandLine.hasOption(CLIOptions.HELP_SS.toString())) {
            help();
        }
    }

    /**
     * Print help banner with all available options on commandline interface
     *
     */
    public static void help() {
        HelpFormatter formater = new HelpFormatter();
        formater.printHelp("Download Help", options);
        System.exit(0);

    }
}
