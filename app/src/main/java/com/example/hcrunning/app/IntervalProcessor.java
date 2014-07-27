package com.example.hcrunning.app;

import android.content.Context;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shinichi on 2014/07/26.
 */
public class IntervalProcessor implements HCRunningCountDownListener {
  /** Members */
  private int mCurrentIndex = 0;
  private ArrayList<HCRunningCountDownTimer> mCountDownTimerList;
  private TextView mDisplayCountDownView;
  private Context mContext;

  /** Constructor */
  public IntervalProcessor (TextView displayCountDownView, Context context ) {
    mCountDownTimerList = new ArrayList<HCRunningCountDownTimer>();
    mDisplayCountDownView = displayCountDownView;
    mContext = context;
  }

  /**
   * setIntervals() does 3 things:
   * (1) clears the entire (previous) list items of count down timer in order to move on to the next interval sets.
   *     If interval list is empty, this function returns without further operation,
   * (2) puts the all interval data into count down timer list, and
   * (3) initializes the index for the next time this function called
   */
  public void setIntervals(List<TimeInterval> timeIntervalList) {
    mCountDownTimerList.clear();

    if( timeIntervalList.isEmpty() )
      return;

    // Using foreach-loop. Same as following:
    // for (int i = 0; i < timeIntervalList.length; i++)
    // {
    //   TimeInterval interval = timeIntervalList[i];
    //   mCountDownTimerList.add( new HCRunningCountDownTimer(interval.toMilliseconds(), 50, mDisplayCountDownView, mContext, this ) );
    // }
    for( TimeInterval interval : timeIntervalList ) {
      mCountDownTimerList.add( new HCRunningCountDownTimer(interval.toMilliseconds(), 50, mDisplayCountDownView, mContext, this ) );
    }

    this.mCurrentIndex = 0;
  }

  /**
   * start() starts the timer, also checks if list of count down timer is empty or not.
   * If empty it returns without further operation.
   */
  public void start() {
    if( this.mCountDownTimerList.isEmpty() )
      return;

    HCRunningCountDownTimer firstTimer = this.mCountDownTimerList.get(this.mCurrentIndex);
    firstTimer.start();
  }

  /**
   * When paused...
   */
  public void pause() {}

  /**
   * When canceled...
   */
  public void cancel() {}

  /**
   * onIntervalFinished() does two things:
   * (1) increments current index; if current index reaches to the end of the list of count down timer,
   *     then this function returns without further operations
   * (2) sets the next interval and starts it
   */
  public void onIntervalFinished() {
    this.mCurrentIndex++;
    if( this.mCurrentIndex == this.mCountDownTimerList.size() )
      return;

    HCRunningCountDownTimer nextTime = this.mCountDownTimerList.get(this.mCurrentIndex);
    nextTime.start();
  }
}
