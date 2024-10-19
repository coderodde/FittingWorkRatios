package fi.helsinki.coderodde.msc;

import static java.lang.Math.abs;
import java.util.List;

class SemiVerboseRunningTimeStatisticsProducer {
    
    void run(final List<DataSet> dataSetList) {
        System.out.println("<<< SemiVerboseRunningTimeStatisticsProducer >>>");
        
        int dataSetNumber = 1;
        
        for (final DataSet dataSet : dataSetList) {
            double closestMean = Double.POSITIVE_INFINITY;
            double smallestStd = Double.POSITIVE_INFINITY;
            double smallestDst = Double.POSITIVE_INFINITY;
            
            double closestMeanGamma = Double.NaN;
            double smallestStdGamma = Double.NaN;
            double smallestDstGamma = Double.NaN;
            
            for (double gamma = 0.0; gamma < 1.0; gamma += 0.01) {
                final RunningTime runningTime = 
                        new SemiVerboseRunningTime(gamma);
                
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
                    closestMeanGamma = gamma;
                }
                
                if (smallestStd > std) {
                    smallestStd = std;
                    smallestStdGamma = gamma;
                }
                
                if (smallestDst > dist) {
                    smallestDst = dist;
                    smallestDstGamma = gamma;
                }
            }
            
            System.out.printf("Data set %3d:\n", dataSetNumber++);
            
            System.out.printf("    Closest mean = %f,\n", closestMean);
            System.out.printf("    Closest mean gamma = %f,\n", 
                              closestMeanGamma);
            
            System.out.printf("    Smallest std = %f,\n", smallestStd);
            System.out.printf("    Smallest std gamma = %f,\n", 
                              smallestStdGamma);
            
            System.out.printf("    Smallest distance = %f,\n", smallestDst);
            System.out.printf("    Smallest distance gamma = %f,\n\n", 
                              smallestDstGamma);
        }
    }
}
