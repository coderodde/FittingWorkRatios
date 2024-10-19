package fi.helsinki.coderodde.msc;

import static java.lang.Math.abs;
import java.util.List;

class VerboseRunningTimeStatisticsProducer {
    
    void run(final List<DataSet> dataSetList) {
        System.out.println("<<< VerboseRunningTimeStatisticsProducer >>>");
        
        int dataSetNumber = 1;
        
        for (final DataSet dataSet : dataSetList) {
            double closestMean = Double.POSITIVE_INFINITY;
            double smallestStd = Double.POSITIVE_INFINITY;
            double smallestDst = Double.POSITIVE_INFINITY;
            
            double closestMeanRho = Double.NaN;
            double smallestStdRho = Double.NaN;
            double smallestDstRho = Double.NaN;
            
            for (double rho = 0.0; rho < 10.0; rho += 0.1) {
                final RunningTime runningTime = new VerboseRunningTime(rho);
                
                final FittingCurve fittingCurve = 
                        FittingCurve.inferFittingCurve(
                                dataSet, 
                                runningTime);
                
                double mean = fittingCurve.mean();
                double std  = fittingCurve.std();
                double dist = fittingCurve.averageDistance(dataSet,
                                                           runningTime);

                if (abs(closestMean - 1.0) > abs(mean - 1.0)) {
                    closestMean = mean;
                    closestMeanRho = rho;
                }
                
                if (smallestStd > std) {
                    smallestStd = std;
                    smallestStdRho = rho;
                }
                
                if (smallestDst > dist) {
                    smallestDst = dist;
                    smallestDstRho = rho;
                }
            }
            
            System.out.printf("Data set %3d:\n", dataSetNumber++);
            
            System.out.printf("    Closest mean = %f,\n", closestMean);
            System.out.printf("    Closest mean rho = %f,\n", 
                              closestMeanRho);
            
            System.out.printf("    Smallest std = %f,\n", smallestStd);
            System.out.printf("    Smallest std rho = %f,\n", 
                              smallestStdRho);
            
            System.out.printf("    Smallest distance = %f,\n", smallestDst);
            System.out.printf("    Smallest distance rho = %f,\n\n", 
                              smallestDstRho);
        }
    }
}
