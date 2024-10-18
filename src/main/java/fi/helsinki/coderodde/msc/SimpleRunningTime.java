package fi.helsinki.coderodde.msc;

class SimpleRunningTime implements RunningTime {

    @Override
    public double estimate(double entropy, int fingers) {
        return Math.pow((double) fingers, 2.0 - entropy);
    }
}
