package fi.helsinki.coderodde.msc;

final class PartialRunningTime implements RunningTime {

    private static final RunningTime RUNNING_TIME = 
            new SemiVerboseRunningTime(0.5);
    
    @Override
    public double estimate(double entropy, int fingers) {
        return RUNNING_TIME.estimate(entropy, fingers);
    }
}
