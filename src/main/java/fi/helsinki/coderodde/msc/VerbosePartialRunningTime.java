package fi.helsinki.coderodde.msc;

import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.log;
import static java.lang.Math.pow;
import static java.lang.Math.PI;

class VerbosePartialRunningTime implements RunningTime {
    
    private final double rho;
    
    VerbosePartialRunningTime(final double rho) {
        this.rho = rho;
    }

    @Override
    public double estimate(double entropy, int fingers) {
        final double factor1 = 1.0 - 0.5 * pow(cos(PI * entropy), 2.0);
        final double factor2 = 1.0 - 0.6 * pow(2.0, rho) 
                                         * pow(abs(entropy - 0.5), rho);
        
        return pow((double) fingers, 
                   2.0 - entropy + log(factor1 * factor2) / log(fingers));
    }
}
