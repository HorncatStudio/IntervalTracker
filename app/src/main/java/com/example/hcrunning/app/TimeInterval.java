package com.example.hcrunning.app;

/**
 * Created by Shinichi on 2014/07/26.
 */
public class TimeInterval {

  public long Minutes = 0;
  public long Seconds = 0;

  public TimeInterval(long minutes, long seconds)
  {
    Minutes = minutes;
    Seconds = seconds;
  }

  public String toString() {
    return  Long.toString(Minutes) + ":" + Long.toString(Seconds);
  }

  public long toMiliseconds() {
    return ( (Minutes* 60) + Seconds ) * 1000;
  }

}
