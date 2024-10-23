package fi.helsinki.coderodde.msc;

import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

class VerbosePartialRunningTimeStatisticsProducer {
    
    void run(final List<DataSet> dataSetList) {
        System.out.println("<<< VerboseRunningTimeStatisticsProducer >>>");
        
        int dataSetNumber = 1;
        final Map<Double, List<Integer>> fittingCurveMeanMap = new TreeMap<>();
        final Map<Double, List<Integer>> fittingCurveStdMap  = new TreeMap<>();
        final Map<Double, List<Integer>> fittingCurveDistMap = new TreeMap<>();
        final Map<Double, List<Integer>> dataSetMeanMap      = new TreeMap<>();
        final Map<Double, List<Integer>> dataSetStdMap       = new TreeMap<>();
        
        for (double rho = 0.0; rho < 10.0; rho += 0.1) {
            fittingCurveMeanMap.put(rho, new ArrayList<>());
            fittingCurveStdMap .put(rho, new ArrayList<>());
            fittingCurveDistMap.put(rho, new ArrayList<>());
            
            dataSetMeanMap.put(rho, new ArrayList<>());
            dataSetStdMap .put(rho, new ArrayList<>());
        }
        
        for (final DataSet dataSet : dataSetList) {
            double fittingCurveClosestMean = Double.POSITIVE_INFINITY;
            double fittingCurveSmallestStd = Double.POSITIVE_INFINITY;
            double fittingCurveSmallestDst = Double.POSITIVE_INFINITY;
            double dataSetClosestMean      = Double.POSITIVE_INFINITY;
            double dataSetSmallestStd      = Double.POSITIVE_INFINITY;
            
            double fittingCurveClosestMeanRho = Double.NaN;
            double fittingCurveSmallestStdRho = Double.NaN;
            double fittingCurveSmallestDstRho = Double.NaN;
            double dataSetClosestMeanRho      = Double.NaN;
            double dataSetSmallestStdRho      = Double.NaN;
            
            FittingCurve optimalFittingCurve = null;
            
            for (double rho = 0.0; rho < 10.0; rho += 0.1) {
                final RunningTime runningTime =
                        new VerbosePartialRunningTime(rho);
                
                final DataSet normalizedDataSet = 
                        dataSet.normalize(runningTime);
                
                final FittingCurve fittingCurve = 
                        FittingCurve.inferFittingCurve(normalizedDataSet);
                
                double fittingCurveMean     = fittingCurve.mean();
                double fittingCurveStd      = fittingCurve.std();
                double fittingCurveDistance = fittingCurve
                                                .averageDistance(
                                                        normalizedDataSet);
                
                double dataSetMean = normalizedDataSet.mean();
                double dataSetStd  = normalizedDataSet.std();

                if (abs(fittingCurveClosestMean - 1.0) > abs(fittingCurveMean - 1.0)) {
                    fittingCurveClosestMean = fittingCurveMean;
                    fittingCurveClosestMeanRho = rho;
                }
                
                if (fittingCurveSmallestStd > fittingCurveStd) {
                    fittingCurveSmallestStd = fittingCurveStd;
                    fittingCurveSmallestStdRho = rho;
                }
                
                if (fittingCurveSmallestDst > fittingCurveDistance) {
                    fittingCurveSmallestDst = fittingCurveDistance;
                    fittingCurveSmallestDstRho = rho;
                }
                
                if (abs(dataSetClosestMean - 1.0) > abs(dataSetMean - 1.0)) {
                    dataSetClosestMean = dataSetMean;
                    dataSetClosestMeanRho = rho;
                    optimalFittingCurve = fittingCurve;
                }
                
                if (dataSetSmallestStd > dataSetStd) {
                    dataSetSmallestStd = dataSetStd;
                    dataSetSmallestStdRho = rho;
                }
            }
            
            System.out.printf("Data set %3d:\n", dataSetNumber);
            
            System.out.printf("  Optimal fitting curve: %s\n",
                              optimalFittingCurve);
            
            System.out.printf("    Closest fitting curve mean = %f,\n", 
                             fittingCurveClosestMean);
            System.out.printf("    Closest fitting curve mean rho = %f,\n", 
                              fittingCurveClosestMeanRho);
            
            System.out.printf("    Smallest fitting curve std = %f,\n",
                             fittingCurveSmallestStd);
            
            System.out.printf("    Smallest fitting curve std rho = %f,\n", 
                              fittingCurveSmallestStdRho);
            
            System.out.printf("    Smallest fitting curve distance = %f,\n", 
                              fittingCurveSmallestDst);
            
            System.out.printf("    Smallest fitting curve distance rho = %f,\n\n", 
                              fittingCurveSmallestDstRho);
            
            System.out.printf("    Closest data set mean: %f,\n", 
                              dataSetClosestMean);
            
            System.out.printf("    Closest data set mean rho = %f,\n",
                              dataSetClosestMeanRho);
            
            System.out.printf("    Smallest data set std: %f,\n", 
                              dataSetSmallestStd);
            
            System.out.printf("    Smallest data set std rho = %f,\n\n",
                              dataSetSmallestStdRho);
            
            fittingCurveMeanMap.get(fittingCurveClosestMeanRho)
                               .add(dataSetNumber);
            
            fittingCurveStdMap .get(fittingCurveSmallestStdRho)
                               .add(dataSetNumber);
            
            fittingCurveDistMap.get(fittingCurveSmallestDstRho)
                               .add(dataSetNumber);
            
            dataSetMeanMap.get(dataSetClosestMeanRho).add(dataSetNumber);
            dataSetStdMap .get(dataSetSmallestStdRho).add(dataSetNumber);
            
            dataSetNumber++;
        }
        
        System.out.println();
        System.out.println("--- Fitting curve means:");
        
        for (final Map.Entry<Double, List<Integer>> e : 
                fittingCurveMeanMap.entrySet()) {
            
            System.out.printf("    rho = %f, indices[%d] = %s\n", 
                              e.getKey(), 
                              e.getValue().size(),
                              e.getValue());
        }
        
        System.out.println();
        System.out.println("--- Fitting curve stds:");
        
        for (final Map.Entry<Double, List<Integer>> e : 
                fittingCurveStdMap.entrySet()) {
            
            System.out.printf("    rho = %f, indices[%d] = %s\n", 
                              e.getKey(), 
                              e.getValue().size(),
                              e.getValue());
        }
        
        System.out.println();
        System.out.println("--- Fitting curve distances:");
        
        for (final Map.Entry<Double, List<Integer>> e : 
                fittingCurveDistMap.entrySet()) {
            
            System.out.printf("    rho = %f, indices[%d] = %s\n", 
                              e.getKey(), 
                              e.getValue().size(),
                              e.getValue());
        }
        
        System.out.println();
        System.out.println("--- Data set means:");
        
        for (final Map.Entry<Double, List<Integer>> e :
                dataSetMeanMap.entrySet()) {
            
            System.out.printf("    rho = %f, indices[%d] = %s\n", 
                              e.getKey(), 
                              e.getValue().size(),
                              e.getValue());
        }
        
        System.out.println();
        System.out.println("--- Data set stds:");
        
        for (final Map.Entry<Double, List<Integer>> e :
                dataSetStdMap.entrySet()) {
            
            System.out.printf("    rho = %f, indices[%d] = %s\n", 
                              e.getKey(), 
                              e.getValue().size(),
                              e.getValue());
        }

        final RunningTime runningTime = new VerbosePartialRunningTime(3.1);
        final DataSet texDataSet = 
                dataSetList
                        .get(39)
                        .normalize(runningTime)
                        .pruneHalf();
        
        System.out.println();
        System.out.println("TeXdataSet:");
        System.out.println(texDataSet);
    }
}
