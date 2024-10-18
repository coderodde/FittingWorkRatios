package fi.helsinki.coderodde.msc;

import static java.lang.Math.abs;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;

/**
 * This class implements the fitting curve for the entropy data that is a 
 * polynomial of order two ({@code aH^2 + bH + c}).
 */
class FittingCurve {
    
    // Fitter for polynomials of degree 2:
    private static final PolynomialCurveFitter FITTER = 
            PolynomialCurveFitter.create(2);
    
    private final double a;
    private final double b;
    private final double c;
    private final int fingers;
    
    FittingCurve(final double a,
                 final double b,
                 final double c,
                 final int fingers) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.fingers = fingers;
    }
    
    double mean() {
        return (a / 3.0) + (b / 2.0) + c;
    }
    
    double std() {
        double x = (4 * a * a / 45.0) + 
                   (a * b / 6.0) + 
                   (b * b / 12.0);
        
        return Math.sqrt(x);
    }
    
    double evaluate(final double entropy) {
        return a * entropy * entropy + b * entropy + c;
    }
    
    double averageDistance(final DataSet dataSet, 
                           final RunningTime runningTime) {
        
        double distanceSum = 0.0;
        
        for (int i = 0; i < dataSet.size(); i++) {
            final DataLine dataLine = dataSet.get(i);
            final double entropy = dataLine.getEntropy();
            final double workRatio = 
                    convertDataLineToWorkRatio(
                            dataLine, 
                            runningTime, 
                            fingers);
            
            final double p = evaluate(entropy);
            distanceSum += abs(p - workRatio);
        }
        
        return distanceSum / dataSet.size();
    }
    
    static FittingCurve inferFittingCurve(final DataSet dataSet,
                                          final RunningTime runningTime) {
        
        final WeightedObservedPoints wop = new WeightedObservedPoints();
        
        for (int i = 0; i < dataSet.size(); i++) {
            final DataLine dataLine = dataSet.get(i);
            final double r = 
                    convertDataLineToWorkRatio(
                            dataLine, 
                            runningTime, 
                            dataSet.getNumberOfFingers());
                
            wop.add(dataLine.getEntropy(), r);
        }
        
        // Parabola curve fitter:
        final double[] coefficients = FITTER.fit(wop.toList());
        
        return new FittingCurve(coefficients[2],
                                coefficients[1],
                                coefficients[0],
                                dataSet.getNumberOfFingers());
    }
    
    private static double 
        convertDataLineToWorkRatio(final DataLine dataLine, 
                                   final RunningTime runningTime,
                                   final int fingers) {
            
        final double work = dataLine.getWork();
        final double entropy = dataLine.getEntropy();
        return work / runningTime.estimate(entropy, fingers);
    }
}
