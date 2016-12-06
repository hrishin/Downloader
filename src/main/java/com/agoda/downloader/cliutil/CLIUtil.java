package com.agoda.downloader.cliutil;

import org.apache.commons.cli.*;

/**
 * Utility class to initialize, extract CLI options
 */
public class CLIUtil {
    public static final String DOWNLOAD_HELP = "Download Help";
    public static final String DOWNLOAD_SOURCE_DESCRIPTION = "Download data sources in csv form";
    private static final String DOWNLOAD_PATH_DESCRIPTION = "Local download path";
    public static final String HELP_DESCRIPTION = "Help";
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
                                            true, DOWNLOAD_PATH_DESCRIPTION);
        Option inputDataOption  = new Option(CLIOptions.DOWNLOAD_SOURCE_SS.toString(), CLIOptions.DOWNLOAD_SOURCE_LS.toString(),
                                            true, DOWNLOAD_SOURCE_DESCRIPTION);
        Option helpOption 		= new Option(CLIOptions.HELP_SS.toString(), HELP_DESCRIPTION);

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
        formater.printHelp(DOWNLOAD_HELP, options);
        System.exit(0);

    }
}
