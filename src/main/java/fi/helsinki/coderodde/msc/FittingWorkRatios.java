package fi.helsinki.coderodde.msc;

import java.io.IOException;
import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.log;
import static java.lang.Math.pow;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This program is used for fitting with parabola the work ratio plots.
 * 
 * @author rodde (Oct 17, 2024)
 * @version 1.0.0
 */
public class FittingWorkRatios {

    public static void main(String[] args) {
        final String fileName = args[0];
        final DataSetsParser dataSetsParser = new DataSetsParser(fileName);
        final List<DataSet> dataSets = dataSetsParser.parse();
        
        for (final DataSet dataSet : dataSets) {
            System.out.println(dataSet);
        }
    }
    
    static double getSimpleRunningTime(int n, double entropy) {
        return Math.pow(n, 2 - entropy);
    }
    
    static double getSemiVerboseTime(int n, double entropy, double gamma) {
        double factor = log(1.0 - gamma * pow(cos(PI * entropy), 2.0)) / log(n);
        return pow(n, 2 - entropy + factor);
    }
    
    static double getVerboseTime(int n, double entropy, double rho) {
        double factor1 = 1.0 - 0.5 * pow(cos(PI * entropy), 2.0);
        double factor2 = 1.0 - 0.6 * pow(2.0, rho) * pow(abs(entropy - 0.5), rho);
        return pow(n, 2 - entropy + factor1 * factor2);
    }
}
