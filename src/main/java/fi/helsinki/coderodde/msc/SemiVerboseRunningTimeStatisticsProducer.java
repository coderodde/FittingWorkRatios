package fi.helsinki.coderodde.msc;

import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

class SemiVerboseRunningTimeStatisticsProducer {
    
    void run(final List<DataSet> dataSetList) {
        System.out.println("<<< SemiVerboseRunningTimeStatisticsProducer >>>");
        
        int dataSetNumber = 1;
        final Map<Double, List<Integer>> meanMap = new TreeMap<>();
        final Map<Double, List<Integer>> stdMap  = new TreeMap<>();
        final Map<Double, List<Integer>> distMap = new TreeMap<>();
        
        for (double gamma = 0.0; gamma < 1.0; gamma += 0.01) {
            meanMap.put(gamma, new ArrayList<>());
            stdMap .put(gamma, new ArrayList<>());
            distMap.put(gamma, new ArrayList<>());
        }
        
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
            
            System.out.printf("Data set %3d:\n", dataSetNumber);
            
            System.out.printf("    Closest mean = %f,\n", closestMean);
            System.out.printf("    Closest mean gamma = %f,\n", 
                              closestMeanGamma);
            
            System.out.printf("    Smallest std = %f,\n", smallestStd);
            System.out.printf("    Smallest std gamma = %f,\n", 
                              smallestStdGamma);
            
            System.out.printf("    Smallest distance = %f,\n", smallestDst);
            System.out.printf("    Smallest distance gamma = %f,\n\n", 
                              smallestDstGamma);
            
            meanMap.get(closestMeanGamma).add(dataSetNumber);
            stdMap .get(smallestStdGamma).add(dataSetNumber);
            distMap.get(smallestDstGamma).add(dataSetNumber);
            
            dataSetNumber++;
        }
        
        System.out.println();
        System.out.println("Means:");
        
        for (final Map.Entry<Double, List<Integer>> e : meanMap.entrySet()) {
            System.out.printf("    gamma = %.3f, indices[%d] = %s\n", 
                              e.getKey(), 
                              e.getValue().size(),
                              e.getValue());
        }
        
        System.out.println();
        System.out.println("Stds:");
        
        for (final Map.Entry<Double, List<Integer>> e : stdMap.entrySet()) {
            System.out.printf("    gamma = %.3f, indices[%d] = %s\n", 
                              e.getKey(), 
                              e.getValue().size(),
                              e.getValue());
        }
        
        System.out.println();
        System.out.println("Distances:");
        
        for (final Map.Entry<Double, List<Integer>> e : distMap.entrySet()) {
            System.out.printf("    gamma = %.3f, indices[%d] = %s\n", 
                              e.getKey(), 
                              e.getValue().size(),
                              e.getValue());
        }
    }
}
