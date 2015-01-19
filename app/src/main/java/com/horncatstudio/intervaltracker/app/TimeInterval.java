package com.horncatstudio.intervaltracker.app;

import java.util.concurrent.TimeUnit;

/**
 * An interval of time stored as Minutes and Seconds.
 */
public class TimeInterval {
  /** Members */
  public long Minutes = 0;
  public long Seconds = 0;

  /** Constructor */
  public TimeInterval( Integer milliseconds )
  {
    Minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
    Seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MILLISECONDS.toSeconds(Minutes);
  }


  public TimeInterval(long minutes, long seconds)
  {
    Minutes = minutes;
    Seconds = seconds;
  }

  /**
   * Returns a string of the time in the format of "m:ss".
   */
  public String toString() {
    if(Seconds < 10) {
      return Long.toString(Minutes) + ":0" + Long.toString(Seconds);
    }
    return  Long.toString(Minutes) + ":" + Long.toString(Seconds);
  }

  /**
   * Returns the current time in a total of milliseconds.
   * @return
   */
  public long toMilliseconds() {
    return ( (Minutes* 60) + Seconds ) * 1000;
  }

}
