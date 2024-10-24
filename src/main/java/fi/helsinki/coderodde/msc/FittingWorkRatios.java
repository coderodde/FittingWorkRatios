package fi.helsinki.coderodde.msc;

import java.io.PrintWriter;
import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 * This program is used for fitting with parabola the work ratio plots.
 * 
 * @author rodde (Oct 17, 2024)
 * @version 1.0.0
 */
public class FittingWorkRatios {
    
    private static final int NUMBER_OF_FINGERS = 100;

    public static void main(String[] args) {
        final Options options = createOptions();
        final CommandLineParser parser = new DefaultParser();
        final CommandLine commandLine;
        printHelp(options);
        System.exit(0);
        try {
            commandLine = parser.parse(options, args);
        } catch (Exception ex) {
            printHelp(options);
            return;
        }
        
        if (!commandLine.hasOption("s")  &&
            !commandLine.hasOption("p")  &&
            !commandLine.hasOption("vp") &&
            !commandLine.hasOption("sv")) {
            return;
        }
        
        
        final String fileName = commandLine.getOptionValue("d");
        final DataSetsParser dataSetsParser = 
                new DataSetsParser(NUMBER_OF_FINGERS, 
                                   fileName);
        
        final List<DataSet> dataSets = dataSetsParser.parse();
        
        if (commandLine.hasOption("s")) {
            new SimpleRunningTimeStatisticsProducer().run(dataSets);
        }
        
        if (commandLine.hasOption("p")) {
            new PartialRunningTimeStatisticsProducer().run(dataSets);
        }
        
        if (commandLine.hasOption("vp")) {
            new VerbosePartialRunningTimeStatisticsProducer().run(dataSets);
        }
            
        if (commandLine.hasOption("sv")) {
            new SemiVerboseRunningTimeStatisticsProducer().run(dataSets);
        }
    }
    
    private static Options createOptions() {
        final Options options = new Options();
        
        final Option optionDataFile =
                Option.builder()
                      .option("d")
                      .desc("specifies the data file")
                      .hasArg(true)
                      .argName("FILE")
                      .longOpt("data-file")
                      .required()
                      .build();
        
        options.addOption(optionDataFile);
        
        final Option optionPartial = 
                Option.builder()
                      .option("p")
                      .longOpt("partial")
                      .desc("run partial running time simulation")
                      .required(false)
                      .build();
        
        options.addOption(optionPartial);
        
        final Option optionSimple = 
                Option.builder()
                      .option("s")
                      .longOpt("simple")
                      .desc("run simple running time simulation")
                      .required(false)
                      .build();
        
        options.addOption(optionSimple);
        
        final Option optionVerbosePartial = 
                Option.builder()
                      .option("vp")
                      .longOpt("verbose-partial")
                      .desc("run verbose-partial running time simulation")
                      .required(false)
                      .build();
        
        options.addOption(optionVerbosePartial);
        
        final Option optionSemiVerbose = 
                Option.builder()
                      .option("sv")
                      .longOpt("semi-verbose")
                      .desc("run semi-verbose running time simulation")
                      .required(false)
                      .build();
        
        options.addOption(optionSemiVerbose);
        
        final Option optionHelp = 
                Option.builder()
                      .option("h")
                      .longOpt("help")
                      .desc("print the heelp message")
                      .required(false)
                      .build();
        
        options.addOption(optionHelp);
        
        return options;
    }
    
    private static void printHelp(final Options options) {
        final HelpFormatter helpFormatter = new HelpFormatter();
        final PrintWriter printWriter = new PrintWriter(System.out);
        helpFormatter.printUsage(printWriter, 
                                 80,
                                 "java -jar FittingWorkRatios.jar",
                                 options);  
        
        helpFormatter.printOptions(printWriter, 80, options, 4, 2);
        printWriter.flush();
    }
}
