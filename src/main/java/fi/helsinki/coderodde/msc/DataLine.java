package fi.helsinki.coderodde.msc;

final class DataLine {
    private final double entropy;
    private final double workRatio;
    
    DataLine(final double entropy, final double workRatio) {
        this.entropy = entropy;
        this.workRatio = workRatio;
    }
    
    double getEntropy() {
        return entropy;
    }
    
    double getWorkRatio() {
        return workRatio;
    }
    
    DataLine normalize(final RunningTime runningTime,
                       final int fingers) {
        final double ratio = workRatio / runningTime.estimate(entropy, fingers);
        return new DataLine(entropy, ratio);
    }
    
    @Override
    public String toString() {
        return String.format("%f %f", 
                             entropy, 
                             workRatio);
    }
}