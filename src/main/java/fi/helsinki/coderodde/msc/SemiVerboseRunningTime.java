package fi.helsinki.coderodde.msc;

import static java.lang.Math.cos;
import static java.lang.Math.log;
import static java.lang.Math.pow;
import static java.lang.Math.PI;

class SemiVerboseRunningTime implements RunningTime {

    private final double gamma;
    
    SemiVerboseRunningTime(final double gamma) {
        this.gamma = gamma;
    }
    
    @Override
    public double estimate(double entropy, int fingers) {
        final double p = 
                2.0 
                - entropy 
                + log(1.0 - gamma * (pow(cos(PI * entropy), 2.0))) /
                  log(fingers);
        
        return pow(fingers, p);
    }
}
