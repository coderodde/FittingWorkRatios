package fi.helsinki.coderodde.msc;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class DataSet {
    
    private final List<DataLine> dataLines = new ArrayList<>();
    private final int fingers;
    
    DataSet(final int fingers) {
        this.fingers = fingers;
    }
    
    DataSet normalize(final RunningTime runningTime) {
        final DataSet normalizedDataSet = new DataSet(fingers);
        
        for (final DataLine dataLine : dataLines) {
            normalizedDataSet.addDataLine(
                    dataLine.normalize(
                            runningTime, 
                            fingers));
        }
        
        return normalizedDataSet;
    }
    
    void addDataLine(final DataLine dataLine) {
        dataLines.add(
                Objects.requireNonNull(
                    dataLine, 
                    "The input data line is null."));
    }
    
    DataLine get(final int index) {
        return dataLines.get(index);
    }
    
    int size() {
        return dataLines.size();
    }
    
    int getNumberOfFingers() {
        return fingers;
    }
    
    double mean() {
        double sum = 0.0;
        
        for (final DataLine dataLine : dataLines) {
            sum += dataLine.getWorkRatio();
        }
        
        return sum / dataLines.size();
    }
    
    double std() {
        final double mean = mean();
        double sum = 0.0;
        
        for (final DataLine dataLine : dataLines) {
            final double term = dataLine.getWorkRatio() - mean;
            sum += term * term;
        }
        
        return Math.sqrt(sum / dataLines.size());
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        
        for (final DataLine dataLine : dataLines) {
            sb.append(dataLine);
            sb.append("\n");
        }
        
        sb.append("\n");
        return sb.toString().replaceAll(",", "\\.");
    }
}
