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
            DataSet optimalDataSet = null;
            
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
                    optimalDataSet = normalizedDataSet;
                    optimalFittingCurve = fittingCurve;
                }
                
                if (dataSetSmallestStd > dataSetStd) {
                    dataSetSmallestStd = dataSetStd;
                    dataSetSmallestStdGamma = gamma;
                }
            }
            
            final DataStatisticsHolder holder = 
                    new DataStatisticsHolder(
                            dataSetNumber, 
                            optimalDataSet, 
                            optimalFittingCurve, 
                            fittingCurveClosestMeanGamma, 
                            fittingCurveSmallestStdGamma,
                            fittingCurveSmallestDstGamma, 
                            dataSetClosestMeanGamma, 
                            dataSetSmallestStdGamma);
            
            dataStatisticsHolderList.add(holder);
            System.out.println(holder);
            
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
            
            System.out.printf("    gamma = %.2f, indices[%d] = %s\n", 
                              e.getKey(), 
                              e.getValue().size(),
                              e.getValue());
        }
        
        System.out.println();
        System.out.println("--- Fitting curve stds:");
        
        for (final Map.Entry<Double, List<Integer>> e : 
                fittingCurveStdMap.entrySet()) {
            
            System.out.printf("    gamma = %.2f, indices[%d] = %s\n", 
                              e.getKey(), 
                              e.getValue().size(),
                              e.getValue());
        }
        
        System.out.println();
        System.out.println("--- Fitting curve distances:");
        
        for (final Map.Entry<Double, List<Integer>> e : 
                fittingCurveDistMap.entrySet()) {
            
            System.out.printf("    gamma = %.2f, indices[%d] = %s\n", 
                              e.getKey(), 
                              e.getValue().size(),
                              e.getValue());
        }
        
        System.out.println();
        System.out.println("--- Data set means:");
        
        for (final Map.Entry<Double, List<Integer>> e :
                dataSetMeanMap.entrySet()) {
            
            System.out.printf("    gamma = %.2f, indices[%d] = %s\n", 
                              e.getKey(), 
                              e.getValue().size(),
                              e.getValue());
        }
        
        System.out.println();
        System.out.println("--- Data set stds:");
        
        for (final Map.Entry<Double, List<Integer>> e :
                dataSetStdMap.entrySet()) {
            
            System.out.printf("    gamma = %.2f, indices[%d] = %s\n", 
                              e.getKey(), 
                              e.getValue().size(),
                              e.getValue());
        }
        
        final int[] dataSetNumbers = { 20, 40, 60, 80, 100 };
        
        System.out.println("TeX table:");
        System.out.println(
                getTableTeXCode(
                        dataSetNumbers,
                        dataStatisticsHolderList));
        
        final RunningTime runningTime = new SemiVerboseRunningTime(0.7);
        final DataSet texDataSet = 
                dataSetList
                        .get(99)
                        .normalize(runningTime);
        
        final FittingCurve fittingCurve = 
                FittingCurve.inferFittingCurve(texDataSet);
        
        System.out.println("Data fitting curve: " + fittingCurve);
        System.out.println("Data:");
        System.out.println(texDataSet.pruneHalf());
    }
    
    private static String getTableTeXCode(
            final int[] dataSetNumbers,
            final List<DataStatisticsHolder> dataSetStatisticHolderList) {
        
        final StringBuilder sb = new StringBuilder();
       
        for (final int dataSetNumber : dataSetNumbers) {
            final int dataSetIndex = dataSetNumber - 1;
            final DataStatisticsHolder holder = 
                    dataSetStatisticHolderList.get(dataSetIndex);
            
            sb.append(holder.convertToTeXTableLine()).append("\n");
        }
        
        return sb.toString();
    }
    
    private static final class DataStatisticsHolder {

        private static final char NL = '\n';
        private static final String SEP = " & ";
        
        private final int dataSetNumber;
        private final DataSet dataSet;
        private final FittingCurve fittingCurve;
        // Data set and fitting curve statistics may be asked
        // from the fields above.
        private final double fittingCurveMeanGamma;
        private final double fittingCurveStdGamma;
        private final double fittingCurveDistanceGamma;
        private final double dataSetMeanGamma;
        private final double dataSetStdGamma;

        public DataStatisticsHolder(final int dataSetNumber,
                                    final DataSet dataSet,
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
            
            sb.append("    -----------------------------").append(NL);
            sb.append("    Closest fitting curve mean = ")
              .append(fittingCurve.mean())
              .append(NL);
            
            sb.append("    Closest fitting curve mean gamma = ")
              .append(String.format("%.2f", fittingCurveMeanGamma))
              .append(NL);
            
            sb.append("    -----------------------------").append(NL);
            sb.append("    Closest fitting curve std = ")
              .append(fittingCurve.std())
              .append(NL);
            
            sb.append("    Closest fitting curve std gamma = ")
              .append(String.format("%.2f", fittingCurveStdGamma))
              .append(NL);
            
            sb.append("    -----------------------------").append(NL);
            sb.append("    Closest fitting curve distance = ")
              .append(fittingCurve.averageDistance(dataSet))
              .append(NL);
            
            sb.append("    Closest fitting curve dstance gamma = ")
              .append(String.format("%.2f", fittingCurveDistanceGamma))
              .append(NL);
            
            sb.append("    -----------------------------").append(NL);
            sb.append("    Closest data set mean = ")
              .append(dataSet.mean())
              .append(NL);
            
            sb.append("    Closest data set mean gamma = ")
              .append(String.format("%.2f", dataSetMeanGamma))
              .append(NL);
            
            sb.append("    -----------------------------").append(NL);
            sb.append("    Smallest data set std = ")
              .append(dataSet.std())
              .append(NL);
            
            sb.append("    Smallest data set std gamma = ")
              .append(String.format("%.2f", dataSetStdGamma))
              .append(NL);
            
            return sb.toString().replace(',', '.');
        }
        
        String convertToTeXTableLine() {
            final StringBuilder sb = new StringBuilder();
            
            sb.append(dataSetNumber)
              .append(SEP)
              .append(String.format("%.4f", fittingCurve.mean()))
              .append(SEP)
              .append(String.format("%.2f", fittingCurveMeanGamma))
              .append(SEP)
              .append(String.format("%.4f", fittingCurve.std()))
              .append(SEP)
              .append(String.format("%.2f", fittingCurveStdGamma))
              .append(SEP)
              .append(String.format(
                      "%.4f", 
                      fittingCurve.averageDistance(dataSet)))
              .append(SEP)
              .append(String.format("%.2f", fittingCurveDistanceGamma))
              .append(SEP)
              .append(String.format("%.4f", dataSet.mean()))
              .append(SEP)
              .append(String.format("%.2f", dataSetMeanGamma))
              .append(SEP)
              .append(String.format("%.4f", dataSet.std()))
              .append(SEP)
              .append(String.format("%.2f", dataSetStdGamma))
              .append(" \\\\");
            
            return sb.toString().replace(',', '.');
        }
    }
}
