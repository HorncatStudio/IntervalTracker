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
  //! The current countdown timer being processed within the IntervalProcessor.  Must be reset
  //! to zero once all of the timers are processed.
  private int mCurrentIndex = 0;

  //! The list of countdown timers to be processed
  private ArrayList<HCRunningCountDownTimer> mCountDownTimerList;

  //! The text view that displays the current time being counted down.  It is updated every second.
  private TextView mDisplayCountDownView;

  //! Context that is needed for the count down timer's usage of a media player
  private Context mContext;

  /**
   * Responsible for counting down a provided set of time intervals.  Displays the current time countdown in
   * the @displayCountDownView and uses the @context to create a ding sound once each interval is  complete.
   *
   * @param displayCountDownView The text view that shall display the current time being counted down
   * @param context The context that the media player shall use to create a ding sound
   */
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

    TimeInterval pausedInterval = HCRunningToolkit.getTimeInterval(this.mDisplayCountDownView);
    long totalInMilliSeconds = pausedInterval.toMilliseconds();

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
