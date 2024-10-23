package fi.helsinki.coderodde.msc;

import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

class SemiVerboseRunningTimeStatisticsProducer {
    
    void run(final List<DataSet> dataSetList) {
        System.out.println("<<< SemiVerboseRunningTimeStatisticsProducer >>>");
        
        final List<DataStatisticsHolder> dataStatisticsHolderList = 
                new ArrayList<>();
        
        int dataSetNumber = 1;
        final Map<Double, List<Integer>> fittingCurveMeanMap = new TreeMap<>();
        final Map<Double, List<Integer>> fittingCurveStdMap  = new TreeMap<>();
        final Map<Double, List<Integer>> fittingCurveDistMap = new TreeMap<>();
        final Map<Double, List<Integer>> dataSetMeanMap      = new TreeMap<>();
        final Map<Double, List<Integer>> dataSetStdMap       = new TreeMap<>();
        
        for (double gamma = 0.0; gamma < 1.0; gamma += 0.01) {
            fittingCurveMeanMap.put(gamma, new ArrayList<>());
            fittingCurveStdMap .put(gamma, new ArrayList<>());
            fittingCurveDistMap.put(gamma, new ArrayList<>());
            
            dataSetMeanMap.put(gamma, new ArrayList<>());
            dataSetStdMap .put(gamma, new ArrayList<>());
        }
        
        for (final DataSet dataSet : dataSetList) {
            double fittingCurveClosestMean = Double.POSITIVE_INFINITY;
            double fittingCurveSmallestStd = Double.POSITIVE_INFINITY;
            double fittingCurveSmallestDst = Double.POSITIVE_INFINITY;
            double dataSetClosestMean      = Double.POSITIVE_INFINITY;
            double dataSetSmallestStd      = Double.POSITIVE_INFINITY;
            
            double fittingCurveClosestMeanGamma = Double.NaN;
            double fittingCurveSmallestStdGamma = Double.NaN;
            double fittingCurveSmallestDstGamma = Double.NaN;
            double dataSetClosestMeanGamma      = Double.NaN;
            double dataSetSmallestStdGamma      = Double.NaN;
            
            FittingCurve optimalFittingCurve = null;
            
            for (double gamma = 0.0; gamma < 1.0; gamma += 0.01) {
                final RunningTime runningTime = 
                        new SemiVerboseRunningTime(gamma);
                
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
                    fittingCurveClosestMeanGamma = gamma;
                }
                
                if (fittingCurveSmallestStd > fittingCurveStd) {
                    fittingCurveSmallestStd = fittingCurveStd;
                    fittingCurveSmallestStdGamma = gamma;
                }
                
                if (fittingCurveSmallestDst > fittingCurveDistance) {
                    fittingCurveSmallestDst = fittingCurveDistance;
                    fittingCurveSmallestDstGamma = gamma;
                }
                
                if (abs(dataSetClosestMean - 1.0) > abs(dataSetMean - 1.0)) {
                    dataSetClosestMean = dataSetMean;
                    dataSetClosestMeanGamma = gamma;
                    optimalFittingCurve = fittingCurve;
                }
                
                if (dataSetSmallestStd > dataSetStd) {
                    dataSetSmallestStd = dataSetStd;
                    dataSetSmallestStdGamma = gamma;
                }
            }
            
            final DataStatisticsHolder holder = 
                    new DataStatisticsHolder(
                            dataSet, 
                            dataSetNumber, 
                            optimalFittingCurve, 
                            fittingCurveClosestMeanGamma, 
                            fittingCurveSmallestStdGamma,
                            fittingCurveSmallestDstGamma, 
                            dataSetClosestMeanGamma, 
                            dataSetSmallestStdGamma);
            
            dataStatisticsHolderList.add(holder);
            System.out.println(holder);
            
//            System.out.printf("Data set %3d:\n", dataSetNumber);
//            
//            System.out.printf("  Optimal fitting curve: %s\n",
//                              optimalFittingCurve);
//            
//            System.out.printf("    Closest fitting curve mean = %f,\n", 
//                             fittingCurveClosestMean);
//            System.out.printf("    Closest fitting curve mean gamma = %f,\n", 
//                              fittingCurveClosestMeanGamma);
//            
//            System.out.printf("    Smallest fitting curve std = %f,\n",
//                             fittingCurveSmallestStd);
//            
//            System.out.printf("    Smallest fitting curve std gamma = %f,\n", 
//                              fittingCurveSmallestStdGamma);
//            
//            System.out.printf("    Smallest fitting curve distance = %f,\n", 
//                              fittingCurveSmallestDst);
//            
//            System.out.printf("    Smallest fitting curve distance gamma = %f,\n\n", 
//                              fittingCurveSmallestDstGamma);
//            
//            System.out.printf("    Closest data set mean: %f,\n", 
//                              dataSetClosestMean);
//            
//            System.out.printf("    Closest data set mean gamma = %f,\n",
//                              dataSetClosestMeanGamma);
//            
//            System.out.printf("    Smallest data set std: %f,\n", 
//                              dataSetSmallestStd);
//            
//            System.out.printf("    Smallest data set std gamma = %f,\n\n",
//                              dataSetSmallestStdGamma);
            
            fittingCurveMeanMap.get(fittingCurveClosestMeanGamma)
                               .add(dataSetNumber);
            
            fittingCurveStdMap .get(fittingCurveSmallestStdGamma)
                               .add(dataSetNumber);
            
            fittingCurveDistMap.get(fittingCurveSmallestDstGamma)
                               .add(dataSetNumber);
            
            dataSetMeanMap.get(dataSetClosestMeanGamma).add(dataSetNumber);
            dataSetStdMap .get(dataSetSmallestStdGamma).add(dataSetNumber);
            
            dataSetNumber++;
        }
        
        System.out.println();
        System.out.println("--- Fitting curve means:");
        
        for (final Map.Entry<Double, List<Integer>> e : 
                fittingCurveMeanMap.entrySet()) {
            
            System.out.printf("    gamma = %f, indices[%d] = %s\n", 
                              e.getKey(), 
                              e.getValue().size(),
                              e.getValue());
        }
        
        System.out.println();
        System.out.println("--- Fitting curve stds:");
        
        for (final Map.Entry<Double, List<Integer>> e : 
                fittingCurveStdMap.entrySet()) {
            
            System.out.printf("    gamma = %f, indices[%d] = %s\n", 
                              e.getKey(), 
                              e.getValue().size(),
                              e.getValue());
        }
        
        System.out.println();
        System.out.println("--- Fitting curve distances:");
        
        for (final Map.Entry<Double, List<Integer>> e : 
                fittingCurveDistMap.entrySet()) {
            
            System.out.printf("    gamma = %f, indices[%d] = %s\n", 
                              e.getKey(), 
                              e.getValue().size(),
                              e.getValue());
        }
        
        System.out.println();
        System.out.println("--- Data set means:");
        
        for (final Map.Entry<Double, List<Integer>> e :
                dataSetMeanMap.entrySet()) {
            
            System.out.printf("    gamma = %f, indices[%d] = %s\n", 
                              e.getKey(), 
                              e.getValue().size(),
                              e.getValue());
        }
        
        System.out.println();
        System.out.println("--- Data set stds:");
        
        for (final Map.Entry<Double, List<Integer>> e :
                dataSetStdMap.entrySet()) {
            
            System.out.printf("    gamma = %f, indices[%d] = %s\n", 
                              e.getKey(), 
                              e.getValue().size(),
                              e.getValue());
        }
    }
    
    private static String getTableTeXCode(
            final int[] dataSetNumbers,
            final List<DataStatisticsHolder> dataSetStatisticHolderList) {
        final StringBuilder sb = new StringBuilder();
       
        for (final int dataSetNumber : dataSetNumbers) {
            final int dataSetIndex = dataSetNumber - 1;
            final DataStatisticsHolder holder = 
                    dataSetStatisticHolderList.get(dataSetIndex);
            
            sb.append(holder).append("\n");
        }
        
        return sb.toString();
    }
    
    private static final class DataStatisticsHolder {

        private static final char NL = '\n';
        private static final String SEP = " & ";
        
        private final DataSet dataSet;
        private final int dataSetNumber;
        private final FittingCurve fittingCurve;
        // Data set and fitting curve statistics may be asked
        // from the fields above.
        private final double fittingCurveMeanGamma;
        private final double fittingCurveStdGamma;
        private final double fittingCurveDistanceGamma;
        private final double dataSetMeanGamma;
        private final double dataSetStdGamma;

        public DataStatisticsHolder(final DataSet dataSet,
                                    final int dataSetNumber,
                                    final FittingCurve fittingCurve,
                                    final double fittingCurveMeanGamma,
                                    final double fittingCurveStdGamma,
                                    final double fittingCurveDistanceGamma,
                                    final double dataSetMeanGamma,
                                    final double dataSetStdGamma) {
            this.dataSet = dataSet;
            this.dataSetNumber = dataSetNumber;
            this.fittingCurve = fittingCurve;
            this.fittingCurveMeanGamma = fittingCurveMeanGamma;
            this.fittingCurveStdGamma = fittingCurveStdGamma;
            this.fittingCurveDistanceGamma = fittingCurveDistanceGamma;
            this.dataSetMeanGamma = dataSetMeanGamma;
            this.dataSetStdGamma = dataSetStdGamma;
        }
        
        public DataSet getDataSet() {
            return dataSet;
        }

        public int getDataSetNumber() {
            return dataSetNumber;
        }

        public FittingCurve getFittingCurve() {
            return fittingCurve;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("Data set ")
              .append(dataSetNumber)
              .append(NL);
            
            sb.append("  Optimal fitting curve: ")
              .append(fittingCurve)
              .append(NL);
            
            sb.append("    Closest fitting curve mean = ")
              .append(fittingCurve.mean())
              .append(NL);
            
            sb.append("    Closest fitting curve mean gamma = ")
              .append(fittingCurveMeanGamma)
              .append(NL);
            
            sb.append("    Closest fitting curve std = ")
              .append(fittingCurve.std())
              .append(NL);
            
            sb.append("    Closest fitting curve std gamma = ")
              .append(fittingCurveStdGamma)
              .append(NL);
            
            sb.append("    Closest fitting curve distance = ")
              .append(fittingCurve.averageDistance(dataSet))
              .append(NL);
            
            sb.append("    Closest fitting curve dstance gamma = ")
              .append(fittingCurveDistanceGamma)
              .append(NL);
            
            sb.append("    Closest data set mean = ")
              .append(dataSet.mean())
              .append(NL);
            
            sb.append("    Closest data set mean gamma = ")
              .append(dataSetMeanGamma)
              .append(NL);
            
            sb.append("    Smallest data set std = ")
              .append(dataSet.std())
              .append(NL);
            
            sb.append("    Smallest data set std gamma = ")
              .append(dataSetStdGamma)
              .append(NL);
            
            return sb.toString();
        }
        
        String convertToTeXTableLine() {
            final StringBuilder sb = new StringBuilder();
            
            sb.append(dataSetNumber)
              .append(SEP)
              .append(fittingCurve.mean())
              .append(SEP)
              .append(String.format("%.2f", fittingCurveMeanGamma))
              .append(SEP)
              .append(fittingCurve.std())
              .append(SEP)
              .append(String.format("%.2f", fittingCurveStdGamma))
              .append(SEP)
              .append(fittingCurve.averageDistance(dataSet))
              .append(SEP)
              .append(String.format("%.2f", fittingCurveDistanceGamma))
              .append(SEP)
              .append(dataSet.mean())
              .append(SEP)
              .append(String.format("%.2f", dataSetMeanGamma))
              .append(SEP)
              .append(dataSet.std())
              .append(SEP)
              .append(String.format("%.2f", dataSetStdGamma))
              .append(" \\\\");
            
            return sb.toString();
        }
    }
    
}
