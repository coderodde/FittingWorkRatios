package fi.helsinki.coderodde.msc;

import java.util.List;

class SimpleRunningTimeStatisticsProducer {

    void run(final List<DataSet> dataSetList) {
        System.out.println("<<< SimpleRunningTimeStatisticsProducer >>>");
        
        final RunningTime runningTime = new SimpleRunningTime();
        int dataSetNumber = 1;
        double minimumDistance = Double.POSITIVE_INFINITY;
        int minimumDistanceDataSetNumber = Integer.MAX_VALUE;
        
        for (final DataSet dataSet : dataSetList) {
            final DataSet normalizedDataSet = dataSet.normalize(runningTime);
            final FittingCurve fittingCurve = 
                    FittingCurve.inferFittingCurve(normalizedDataSet);
            
            final double mean = fittingCurve.mean();
            final double std  = fittingCurve.std();
            final double dist = fittingCurve.averageDistance(normalizedDataSet);
            
            if (minimumDistance > dist) {
                minimumDistance = dist;
                minimumDistanceDataSetNumber = dataSetNumber;
            }
            
            System.out.printf("Data set %-3d: mean = %f, std = %f, dist = %f.\n", 
                              dataSetNumber++,
                              mean,
                              std,
                              dist);
        }
        
        System.out.printf("Minimum distance: %f, data set number: %d.\n",
                          minimumDistance, 
                          minimumDistanceDataSetNumber);
        
        // Print the fitting curve:
        final DataSet optimalDataSet = 
                dataSetList
                        .get(63)
                        .normalize(runningTime);
        
        System.out.println("Best data set:");
        
        for (int i = 0; i < optimalDataSet.size(); i++) {
            final DataLine dataLine = optimalDataSet.get(i);
            System.out.println(dataLine);
        }
        
        final FittingCurve fittingCurve = 
                FittingCurve.inferFittingCurve(optimalDataSet);
        
        System.out.printf("Average fitting curve distance: %s.\n",
                          fittingCurve.averageDistance(optimalDataSet));
        
        System.out.println();
    }
}
