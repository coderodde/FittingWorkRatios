package fi.helsinki.coderodde.msc;

class DataLine {
    private final double entropy;
    private final double work;
    
    DataLine(final double entropy, final double workRatio) {
        this.entropy = entropy;
        this.work = workRatio;
    }
    
    double getEntropy() {
        return entropy;
    }
    
    double getWork() {
        return work;
    }
    
    @Override
    public String toString() {
        return String.format("%.4f %.4f", 
                             entropy, 
                             work);
    }
}