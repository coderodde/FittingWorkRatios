package fi.helsinki.coderodde.msc;

import java.util.List;

final class PartialRunningTimeStatisticsProducer {
    
    void run(final List<DataSet> dataSetList) {
        System.out.println("<<< PartialRunningTimeStatisticsProducer >>>");
        
        final RunningTime runningTime = new PartialRunningTime();
        int dataSetNumber = 1;
        double minimumDistance = Double.POSITIVE_INFINITY;
        int minimumDistanceDataSetNumber = Integer.MAX_VALUE;
        
        for (final DataSet dataSet : dataSetList) {
            final DataSet normalizedDataSet = dataSet.normalize(runningTime);
            final FittingCurve fittingCurve = 
                    FittingCurve.inferFittingCurve(normalizedDataSet);
            
            final double curveMean = fittingCurve.mean();
            final double curveStd  = fittingCurve.std();
            final double curveDist = fittingCurve.averageDistance(  
                                                    normalizedDataSet);
            
            final double dataSetMean = normalizedDataSet.mean();
            final double dataSetStd  = normalizedDataSet.std();
            
            if (minimumDistance > curveDist) {
                minimumDistance = curveDist;
                minimumDistanceDataSetNumber = dataSetNumber;
            }
            
            System.out.printf(
                    "[Data set %-3d] curve: mean = %f, std = %f, dist = %f; " + 
                    "data set: mean = %f, std = %f.\n", 
                              dataSetNumber,
                              curveMean,
                              curveStd,
                              curveDist,
                              dataSetMean,
                              dataSetStd);
            dataSetNumber++;
        }
        
        System.out.printf("Minimum distance: %f, data set number: %d.\n",
                          minimumDistance, 
                          minimumDistanceDataSetNumber);
        
        // Print the fitting curve:
        final DataSet optimalDataSet = 
                dataSetList
                        .get(63)
                        .normalize(runningTime);
        
        final FittingCurve fittingCurve = 
                FittingCurve.inferFittingCurve(optimalDataSet);
        
        System.out.printf("Average fitting curve distance: %s.\n",
                          fittingCurve.averageDistance(optimalDataSet));
        
        System.out.println("Fitting curve: " + fittingCurve);
        
        System.out.println("Best data set:");
        System.out.println(optimalDataSet.pruneHalf());
        
        System.out.println();
    }
}
