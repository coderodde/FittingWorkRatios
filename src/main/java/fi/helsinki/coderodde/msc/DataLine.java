package fi.helsinki.coderodde.msc;

class DataLine {
    private final double entropy;
    private final double workRatio;
    
    DataLine(final double entropy, final double workRatio) {
        this.entropy = entropy;
        this.workRatio = workRatio;
    }
    
    @Override
    public String toString() {
        return String.format("%.4f %.4f", 
                             entropy, 
                             workRatio);
    }
}