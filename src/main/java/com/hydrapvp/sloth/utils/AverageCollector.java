package com.hydrapvp.sloth.utils;

import java.util.ConcurrentModificationException;
import java.util.ArrayList;

public class AverageCollector
{
    private ArrayList<Double> averageNumbers;
    
    public AverageCollector() {
        this.averageNumbers = new ArrayList<Double>();
    }
    
    public void add(final double d) {
        this.averageNumbers.add(d);
    }
    
    public double getAverage() {
        double total = 0.0;
        try {
            for (final double d : this.averageNumbers) {
                total += d;
            }
        }
        catch (ConcurrentModificationException cme) {
            return -1.0;
        }
        total /= this.averageNumbers.size();
        return total;
    }
    
    public double getMax() {
        double max = 0.0;
        for (final double d : this.averageNumbers) {
            if (d > max) {
                max = d;
            }
        }
        return max;
    }
    
    public double getMin() {
        double min = 9.9999999E7;
        for (final double d : this.averageNumbers) {
            if (d < min) {
                min = d;
            }
        }
        if (min != Double.MAX_VALUE) {
            return min;
        }
        return 0.0;
    }
    
    public ArrayList<Double> getNumbers() {
        return this.averageNumbers;
    }
    
    public int getDataAmount() {
        return this.averageNumbers.size();
    }
    
    public void clear() {
        this.averageNumbers.clear();
    }
}
