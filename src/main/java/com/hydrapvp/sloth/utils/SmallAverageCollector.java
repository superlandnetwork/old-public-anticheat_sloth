package com.hydrapvp.sloth.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SmallAverageCollector
{
  @SuppressWarnings({ "unchecked", "rawtypes" })
private List<Double> averages = new ArrayList();
  private int collection;
  
  public SmallAverageCollector(int collection)
  {
    this.collection = collection;
  }
  
  @SuppressWarnings("rawtypes")
public void add(double d)
  {
    if (this.averages.size() >= this.collection)
    {
      @SuppressWarnings("unchecked")
	List<Double> newValues = new ArrayList();
      newValues.add(Double.valueOf(d));
      for (int i = 1; i < this.averages.size(); i++) {
        newValues.add(this.averages.get(i - 1));
      }
      this.averages = newValues;
    }
    else
    {
      this.averages.add(Double.valueOf(d));
    }
  }
  
  public double getAverage()
  {
    double total = 0.0D;
    for (@SuppressWarnings("rawtypes")
	Iterator localIterator = this.averages.iterator(); localIterator.hasNext();)
    {
      double d = ((Double)localIterator.next()).doubleValue();
      total += d;
    }
    return total / this.averages.size();
  }
  
  public int size()
  {
    return this.averages.size();
  }
  
  public void clear()
  {
    this.averages.clear();
  }
}
