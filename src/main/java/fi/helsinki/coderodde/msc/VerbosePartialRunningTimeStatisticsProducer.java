package fi.helsinki.coderodde.msc;

import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

class VerbosePartialRunningTimeStatisticsProducer {
    
    void run(final List<DataSet> dataSetList) {
        System.out.println(
                "<<< VerbosePartialRunningTimeStatisticsProducer >>>");
        
        final List<DataStatisticsHolder> dataStatisticsHolderList = 
                new ArrayList<>();
        
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
            DataSet optimalDataSet = null;
            
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
                    optimalDataSet = normalizedDataSet;
                    optimalFittingCurve = fittingCurve;
                }
                
                if (dataSetSmallestStd > dataSetStd) {
                    dataSetSmallestStd = dataSetStd;
                    dataSetSmallestStdRho = rho;
                }
            }
            
            final DataStatisticsHolder holder = 
                    new DataStatisticsHolder(
                            dataSetNumber, 
                            optimalDataSet, 
                            optimalFittingCurve, 
                            fittingCurveClosestMeanRho, 
                            fittingCurveSmallestStdRho,
                            fittingCurveSmallestDstRho, 
                            dataSetClosestMeanRho, 
                            dataSetSmallestStdRho);
            
            dataStatisticsHolderList.add(holder);
            System.out.println(holder);
            
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
        
        final int[] dataSetNumbers = { 20, 40, 60, 80, 100 };
        
        System.out.println("TeX table:");
        System.out.println(
                getTableTeXCode(
                        dataSetNumbers, 
                        dataStatisticsHolderList));

        final RunningTime runningTime = new VerbosePartialRunningTime(3.1);
        final DataSet texDataSet = 
                dataSetList
                        .get(39)
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
        private final double fittingCurveMeanRho;
        private final double fittingCurveStdRho;
        private final double fittingCurveDistanceRho;
        private final double dataSetMeanRho;
        private final double dataSetStdRho;

        public DataStatisticsHolder(final int dataSetNumber,
                                    final DataSet dataSet,
                                    final FittingCurve fittingCurve,
                                    final double fittingCurveMeanRho,
                                    final double fittingCurveStdRho,
                                    final double fittingCurveDistanceRho,
                                    final double dataSetMeanRho,
                                    final double dataSetStdRho) {
            this.dataSet = dataSet;
            this.dataSetNumber = dataSetNumber;
            this.fittingCurve = fittingCurve;
            this.fittingCurveMeanRho = fittingCurveMeanRho;
            this.fittingCurveStdRho = fittingCurveStdRho;
            this.fittingCurveDistanceRho = fittingCurveDistanceRho;
            this.dataSetMeanRho = dataSetMeanRho;
            this.dataSetStdRho = dataSetStdRho;
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
            
            sb.append("    Closest fitting curve mean rho = ")
              .append(String.format("%.1f", fittingCurveMeanRho))
              .append(NL);
            
            sb.append("    -----------------------------").append(NL);
            sb.append("    Closest fitting curve std = ")
              .append(fittingCurve.std())
              .append(NL);
            
            sb.append("    Closest fitting curve std rho = ")
              .append(String.format("%.1f", fittingCurveStdRho))
              .append(NL);
            
            sb.append("    -----------------------------").append(NL);
            sb.append("    Closest fitting curve distance = ")
              .append(fittingCurve.averageDistance(dataSet))
              .append(NL);
            
            sb.append("    Closest fitting curve dstance rho = ")
              .append(String.format("%.1f", fittingCurveDistanceRho))
              .append(NL);
            
            sb.append("    -----------------------------").append(NL);
            sb.append("    Closest data set mean = ")
              .append(dataSet.mean())
              .append(NL);
            
            sb.append("    Closest data set mean rho = ")
              .append(String.format("%.1f", dataSetMeanRho))
              .append(NL);
            
            sb.append("    -----------------------------").append(NL);
            sb.append("    Smallest data set std = ")
              .append(dataSet.std())
              .append(NL);
            
            sb.append("    Smallest data set std rho = ")
              .append(String.format("%.1f", dataSetStdRho))
              .append(NL);
            
            return sb.toString().replace(',', '.');
        }
        
        String convertToTeXTableLine() {
            final StringBuilder sb = new StringBuilder();
            
            sb.append(dataSetNumber)
              .append(SEP)
              .append(String.format("%.4f", fittingCurve.mean()))
              .append(SEP)
              .append(String.format("%.1f", fittingCurveMeanRho))
              .append(SEP)
              .append(String.format("%.4f", fittingCurve.std()))
              .append(SEP)
              .append(String.format("%.1f", fittingCurveStdRho))
              .append(SEP)
              .append(String.format(
                      "%.4f", 
                      fittingCurve.averageDistance(dataSet)))
              .append(SEP)
              .append(String.format("%.1f", fittingCurveDistanceRho))
              .append(SEP)
              .append(String.format("%.4f", dataSet.mean()))
              .append(SEP)
              .append(String.format("%.1f", dataSetMeanRho))
              .append(SEP)
              .append(String.format("%.4f", dataSet.std()))
              .append(SEP)
              .append(String.format("%.1f", dataSetStdRho))
              .append(" \\\\");
            
            return sb.toString().replace(',', '.');
        }
    }
}
