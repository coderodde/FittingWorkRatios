package fi.helsinki.coderodde.msc;

import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

class VerboseRunningTimeStatisticsProducer {
    
    void run(final List<DataSet> dataSetList) {
        System.out.println("<<< VerboseRunningTimeStatisticsProducer >>>");
        
        int dataSetNumber = 1;
        final Map<Double, List<Integer>> meanMap = new TreeMap<>();
        final Map<Double, List<Integer>> stdMap  = new TreeMap<>();
        final Map<Double, List<Integer>> distMap = new TreeMap<>();
        
        for (double rho = 0.0; rho < 10.0; rho += 0.1) {
            meanMap.put(rho, new ArrayList<>());
            stdMap .put(rho, new ArrayList<>());
            distMap.put(rho, new ArrayList<>());
        }
        
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
            
            meanMap.get(closestMeanRho).add(dataSetNumber);
            stdMap .get(smallestStdRho).add(dataSetNumber);
            distMap.get(smallestDstRho).add(dataSetNumber);
        }
        
        System.out.println();
        System.out.println("Means:");
        
        for (final Map.Entry<Double, List<Integer>> e : meanMap.entrySet()) {
            System.out.printf("    rho = %.3f, indices[%3d] = %s\n", 
                              e.getKey(), 
                              e.getValue().size(),
                              e.getValue());
        }
        
        System.out.println();
        System.out.println("Stds:");
        
        for (final Map.Entry<Double, List<Integer>> e : stdMap.entrySet()) {
            System.out.printf("    rho = %.3f, indices[%3d] = %s\n", 
                              e.getKey(), 
                              e.getValue().size(),
                              e.getValue());
        }
        
        System.out.println();
        System.out.println("Distances:");
        
        for (final Map.Entry<Double, List<Integer>> e : distMap.entrySet()) {
            System.out.printf("    rho = %.3f, indices[%3d] = %s\n", 
                              e.getKey(), 
                              e.getValue().size(),
                              e.getValue());
        }
    }
}
