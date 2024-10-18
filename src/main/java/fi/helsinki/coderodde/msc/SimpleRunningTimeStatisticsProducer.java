package fi.helsinki.coderodde.msc;

import java.util.List;

class SimpleRunningTimeStatisticsProducer {

    public void run(final List<DataSet> dataSetList) {
        System.out.println("<<< SimpleRunningTimeStatisticsProducer >>>");
        
        final RunningTime runningTime = new SimpleRunningTime();
        int dataSetNumber = 1;
        
        for (final DataSet dataSet : dataSetList) {
            final FittingCurve fittingCurve = 
                    FittingCurve.inferFittingCurve(
                            dataSet, 
                            runningTime);
            
            final double mean = fittingCurve.mean();
            final double std  = fittingCurve.std();
            final double dist = fittingCurve.averageDistance(dataSet, 
                                                             runningTime);
            
            System.out.printf("Data set %-3d: mean = %f, std = %f, dist = %f.\n", 
                              dataSetNumber++,
                              mean,
                              std,
                              dist);
        }
        
        System.out.println();
    }
}
