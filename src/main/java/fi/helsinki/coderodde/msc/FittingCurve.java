package fi.helsinki.coderodde.msc;

/**
 * This class implements the fitting curve for the entropy data that is a 
 * polynomial of order two ({@code aH^2 + bH + c}).
 */
class FittingCurve {
    private final double a;
    private final double b;
    private final double c;
    
    FittingCurve(final double a,
                 final double b,
                 final double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
    
    double mean() {
        return a / 3.0 + b / 2.0 + c;
    }
    
    double std() {
        double x = 4 * a * a / 45.0 + a * b / 6.0 + b * b / 12.0;
        
        return Math.sqrt(x);
    }
    
    double averageDistance(final DataSet dataSet) {
        double distanceSum = 0.0;
        
        for (int i = 0; i < dataSet.size(); i++) {
            
        }
        
        return distanceSum / dataSet.getFingers();
    }
}
