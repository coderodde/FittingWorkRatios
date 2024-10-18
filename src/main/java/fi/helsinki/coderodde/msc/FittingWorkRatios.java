package fi.helsinki.coderodde.msc;

import java.util.Arrays;
import java.util.List;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;

/**
 * This program is used for fitting with parabola the work ratio plots.
 * 
 * @author rodde (Oct 17, 2024)
 * @version 1.0.0
 */
public class FittingWorkRatios {
    
    private static final int NUMBER_OF_FINGERS = 100;

    public static void main(String[] args) {
//        WeightedObservedPoints wop = new WeightedObservedPoints();
//        
//        wop.add(0.75 - Math.sqrt(41) / 4.0, 0.0);
//        wop.add(0.75 + Math.sqrt(41) / 4.0, 0.0);
//        wop.add(0.75, -41 / 8.0);
//        
//        double[] coeffs = PolynomialCurveFitter.create(2).fit(wop.toList());
//        
//        System.out.println(Arrays.toString(coeffs));
        
        final String fileName = args[0];
        final DataSetsParser dataSetsParser = 
                new DataSetsParser(NUMBER_OF_FINGERS, 
                                   fileName);
        
        final List<DataSet> dataSets = dataSetsParser.parse();
        
        new SimpleRunningTimeStatisticsProducer().run(dataSets);
        new SemiVerboseRunningTimeStatisticsProducer().run(dataSets);
    }
}
