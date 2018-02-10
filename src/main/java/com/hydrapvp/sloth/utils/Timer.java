package com.hydrapvp.sloth.utils;

public class Timer
{
  private long start;
  
  public boolean hasReached(long time)
  {
    return System.currentTimeMillis() > this.start + time;
  }
  
  public void reset()
  {
    this.start = System.currentTimeMillis();
  }
  
  public double getTime()
  {
    return System.currentTimeMillis() - this.start;
  }
}
