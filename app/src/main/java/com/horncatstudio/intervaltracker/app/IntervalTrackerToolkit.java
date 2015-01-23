package com.horncatstudio.intervaltracker.app;

import android.widget.TextView;

/**
 * Provides convenience methods for the app.
 */
public class IntervalTrackerToolkit {
  public static TimeInterval getTimeInterval( TextView displayCountDownView ) {
    String pausedTime = displayCountDownView.getText().toString();
    String[] pausedSplitTime = pausedTime.split(":", 0);

    TimeInterval interval = new TimeInterval( Long.parseLong(pausedSplitTime[0]),
                                              Long.parseLong(pausedSplitTime[1]) );
    return interval;
  }
}
