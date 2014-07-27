package com.example.hcrunning.app;

/**
 * Created by Shinichi on 2014/07/26.
 */
public class TimeInterval {
  /** Members */
  public long Minutes = 0;
  public long Seconds = 0;

  /** Constructor */
  public TimeInterval(long minutes, long seconds)
  {
    Minutes = minutes;
    Seconds = seconds;
  }

  public String toString() {
    return  Long.toString(Minutes) + ":" + Long.toString(Seconds);
  }

  public long toMilliseconds() {
    return ( (Minutes* 60) + Seconds ) * 1000;
  }

}
