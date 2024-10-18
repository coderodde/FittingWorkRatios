package fi.helsinki.coderodde.msc;

import java.util.List;

/**
 * This program is used for fitting with parabola the work ratio plots.
 * 
 * @author rodde (Oct 17, 2024)
 * @version 1.0.0
 */
public class FittingWorkRatios {
    
    private static final int NUMBER_OF_FINGERS = 100;

    public static void main(String[] args) {
        final String fileName = args[0];
        final DataSetsParser dataSetsParser = 
                new DataSetsParser(NUMBER_OF_FINGERS, 
                                   fileName);
        
        final List<DataSet> dataSets = dataSetsParser.parse();
        
        for (final DataSet dataSet : dataSets) {
            System.out.println(dataSet);
        }
    }
}
