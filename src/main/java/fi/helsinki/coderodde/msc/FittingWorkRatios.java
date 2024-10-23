package fi.helsinki.coderodde.msc;

import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * This program is used for fitting with parabola the work ratio plots.
 * 
 * @author rodde (Oct 17, 2024)
 * @version 1.0.0
 */
public class FittingWorkRatios {
    
    private static final int NUMBER_OF_FINGERS = 100;

    public static void main(String[] args) {
        final Options options = new Options();
        
        options.addOption("d", "data-file", true, "The data file to work on");
        
        options.addOption("s",
                          "simple",
                          false,
                          "Run simple running time simulation");
        
        options.addOption("p",
                          "partial", 
                          false, 
                          "Run partial running time simulation");
        
        options.addOption("vp", "verbose-partial", 
                          false, 
                          "Run verbose partial running time simulation");
        
        options.addOption("sv",
                          "semiverbose",
                          false, 
                          "Run semi-verbose running time simulation");
        
        final CommandLineParser parser = new GnuParser();
        final CommandLine commandLine;
        
        try {
            commandLine = parser.parse(options, args);
        } catch (ParseException ex) {
            final HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.printHelp("java -jar FittingWorkRatios.jar", options);
            return;
        }
        
        if (!commandLine.hasOption("d")) {
            System.err.println(
                    "--data option with an argument file name is mandatory.");
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
}
