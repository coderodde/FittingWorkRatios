package fi.helsinki.coderodde.msc;

import static java.lang.Math.abs;

/**
 * This class implements the fitting curve for the entropy data that is a 
 * polynomial of order two ({@code aH^2 + bH + c}).
 */
class FittingCurve {
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
        return a / 3.0 + b / 2.0 + c;
    }
    
    double std() {
        double x = 4 * a * a / 45.0 + a * b / 6.0 + b * b / 12.0;
        
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
            final double work = dataLine.getWork();
            final double entropy = dataLine.getEntropy();
            final double workRatio = work / runningTime.estimate(entropy, 
                                                                 fingers);
            final double p = evaluate(entropy);
            distanceSum += abs(p - workRatio);
        }
        
        return distanceSum / dataSet.getFingers();
    }
}
