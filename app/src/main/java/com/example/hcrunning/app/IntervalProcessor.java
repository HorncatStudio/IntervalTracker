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
    this.mCountDownTimerList.clear();

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
   * stop() stops the timer, also checks if list of count down timer is empty or not.
   * If empty it returns without further operation. Also initialized the current index.
   */
  public void stop() {
    if( this.mCountDownTimerList.isEmpty() )
      return;

    HCRunningCountDownTimer stoppedTimer = this.mCountDownTimerList.get(this.mCurrentIndex);
    stoppedTimer.cancel();

    this.mCurrentIndex = 0;
  }

  /**
   * pause() does four things:
   * (1) cancels the timer,
   * (2) takes the paused time,
   * (3) creates new timer which replaces old one, and
   * (4) adds it to the count down timer list.
   * If count down timer list is empty, this returns without further operations.
   */
  public void pause() {
    if( this.mCountDownTimerList.isEmpty() )
      return;

    HCRunningCountDownTimer pausedTimer = this.mCountDownTimerList.get(this.mCurrentIndex);
    pausedTimer.cancel();

    String pausedTime = this.mDisplayCountDownView.getText().toString();
    String[] pausedSplitTime = pausedTime.split(":", 0);
    long pausedTimeMinute = Long.parseLong(pausedSplitTime[0]);
    long pausedTimeSeconds = Long.parseLong(pausedSplitTime[1]);
    long totalInMilliSeconds = ((pausedTimeMinute * 60) + pausedTimeSeconds) * 1000;

    HCRunningCountDownTimer replacingTimer = new HCRunningCountDownTimer(totalInMilliSeconds, 50, mDisplayCountDownView, mContext, this);

    this.mCountDownTimerList.set(this.mCurrentIndex, replacingTimer);
  }

  /**
   * cancel() does three things:
   * (1) stops the timer,
   * (2) clears the list of count down timer, and
   * (3) resets the text.
   * If count down timer list is empty then resets text and returns without further operations.
   */
  public void cancel() {
    if( this.mCountDownTimerList.isEmpty() ) {
      this.mDisplayCountDownView.setText("");
      this.mDisplayCountDownView.append("Set time and press start!");
      return;
    }

    HCRunningCountDownTimer timer = this.mCountDownTimerList.get(this.mCurrentIndex);
    timer.cancel();

    this.mCountDownTimerList.clear();

    this.mDisplayCountDownView.setText("");
    this.mDisplayCountDownView.append("Set time and press start!");
  }

  /**
   * onIntervalFinished() does two things:
   * (1) increments current index; if current index reaches to the end of the list of count down timer,
   *     then this function clears the list and returns without further operations
   * (2) sets the next interval and starts it
   */
  public void onIntervalFinished() {
    this.mCurrentIndex++;
    if( this.mCurrentIndex == this.mCountDownTimerList.size() ) {
      this.mCountDownTimerList.clear();
      return;
    }

    HCRunningCountDownTimer nextTime = this.mCountDownTimerList.get(this.mCurrentIndex);
    nextTime.start();
  }
}
