package fi.helsinki.coderodde.msc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

class DataSetsParser {
    
    private final int fingers;
    private final String fileName;
    
    DataSetsParser(final int fingers,
                   final String fileName) {
        this.fingers = fingers;
        this.fileName = fileName;
    }
    
    List<DataSet> parse() {
        final List<String> lines;
        
        try {
            lines = Files.readAllLines(Path.of(fileName));
        } catch (IOException ex) {
            Logger.getLogger(DataSetsParser.class.getName())
                  .log(Level.SEVERE, null, ex);
            
            return null;
        }
        
        return parseImpl(lines);
    }
    
    private List<DataSet> parseImpl(final List<String> lines) {
        final List<DataSet> dataSets = new ArrayList<>();
        DataSet currentDataSet = null;
        
        for (int i = 0; i < lines.size(); i++) {
            final String line = lines.get(i).trim();
            
            if (line.startsWith("# r")) {
                // Simply omit the correlation comment:
                continue;
            } else if (line.isBlank()) {
                // Omit blank line:
                continue;
            } else if (line.startsWith("# Iteration")) {
                currentDataSet = new DataSet(fingers);
                dataSets.add(currentDataSet);
            } else {
                final String[] tokens = line.split("\\s+");
                final double entropy = Double.parseDouble(tokens[0]);
                final double ratio   = Double.parseDouble(tokens[1]);
                final DataLine dataLine = new DataLine(entropy,
                                                       ratio);
                
                currentDataSet.addDataLine(dataLine);
            }
        }
        
        return dataSets;
    }
}