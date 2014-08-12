package com.example.hcrunning.app;

import android.widget.TextView;

/**
 * Created by Shinichi on 2014/08/06.
 */
public class HCRunningToolkit {
  public static TimeInterval getTimeInterval( TextView displayCountDownView ) {
    String pausedTime = displayCountDownView.getText().toString();
    String[] pausedSplitTime = pausedTime.split(":", 0);

    TimeInterval interval = new TimeInterval( Long.parseLong(pausedSplitTime[0]),
                                              Long.parseLong(pausedSplitTime[1]) );
    return interval;
  }
}
