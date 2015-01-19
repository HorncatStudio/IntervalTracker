package com.example.hcrunning.app;

import android.content.Context;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Process the intervals provided.  Allows the user to start, stop, pause, and cancel interface processing.
 */
public class IntervalProcessor implements IntervalTimer.IntervalTimerListener {

  /**
   * Listener that can be registered with the IntervalProcessor in order to do an action
   * when an individual interval has been processed.
   */
  public interface IntervalCompleteListener {
    public static final int INTERVAL_FINISHED = -1;
    public static final int INTERVAL_START = 0;
    public void onIntervalComplete( final int intervalIndex );
  }

  /** Members */
  //! The current countdown timer being processed within the IntervalProcessor.  Must be reset
  //! to zero once all of the timers are processed.
  private int mCurrentIndex = 0;

  //! The list of countdown timers to be processed
  private ArrayList<IntervalTimer> mCountDownTimerList;

  //! The text view that displays the current time being counted down.  It is updated every second.
  private TextView mDisplayCountDownView;

  //! Context that is needed for the count down timer's usage of a media player
  private Context mContext;

  private IntervalCompleteListener mIntervalProcessedListener = null;

  /**
   * Responsible for counting down a provided set of time intervals.  Displays the current time countdown in
   * the @displayCountDownView and uses the @context to create a ding sound once each interval is  complete.
   *
   * @param displayCountDownView The text view that shall display the current time being counted down
   * @param context The context that the media player shall use to create a ding sound
   */
  public IntervalProcessor (TextView displayCountDownView, Context context ) {
    mCountDownTimerList = new ArrayList<IntervalTimer>();
    mDisplayCountDownView = displayCountDownView;
    mContext = context;
  }

  public void setCurrentTimeDisplay( TextView displayCurrentTimeView ) {
    this.mDisplayCountDownView = displayCurrentTimeView;
  }

  public void registerIntervalCompleteListener( IntervalCompleteListener listener ) {
    this.mIntervalProcessedListener = listener;
  }

  /**
   * Setting the intervals to be processed. Resets IntervalProcessor.
   */
  public void setIntervals(List<TimeInterval> timeIntervalList) {
    this.mCountDownTimerList.clear();

    if( timeIntervalList.isEmpty() )
      return;

    for( TimeInterval interval : timeIntervalList ) {
      mCountDownTimerList.add( new IntervalTimer(interval.toMilliseconds(), 50, mDisplayCountDownView, mContext, this ) );
    }

    this.mCurrentIndex = 0;
  }

  /**
   * Starts the timers. If empty it returns without further operation.
   */
  public void start() {
    if( this.mCountDownTimerList.isEmpty() )
      return;

    if( mIntervalProcessedListener != null ) {
      mIntervalProcessedListener.onIntervalComplete(IntervalCompleteListener.INTERVAL_START);
    }

    IntervalTimer firstTimer = this.mCountDownTimerList.get(this.mCurrentIndex);
    firstTimer.start();

  }

  /**
   * Stops the timer.  Resets interval to process back to first interval.
   */
  public void stop() {
    if( this.mCountDownTimerList.isEmpty() )
      return;

    IntervalTimer stoppedTimer = this.mCountDownTimerList.get(this.mCurrentIndex);
    stoppedTimer.cancel();

    this.mCurrentIndex = 0;
  }

  /**
   * Pauses the current interval being processed.  To restart processing the interval paused, use the
   * start() method.
   */
  public void pause() {
    if( this.mCountDownTimerList.isEmpty() )
      return;

    IntervalTimer pausedTimer = this.mCountDownTimerList.get(this.mCurrentIndex);
    pausedTimer.cancel();

    TimeInterval pausedInterval = IntervalTrackerToolkit.getTimeInterval(this.mDisplayCountDownView);
    long totalInMilliSeconds = pausedInterval.toMilliseconds();

    IntervalTimer replacingTimer = new IntervalTimer(totalInMilliSeconds, 50, mDisplayCountDownView, mContext, this);

    this.mCountDownTimerList.set(this.mCurrentIndex, replacingTimer);
  }

  /**
   * Clears all the intervals.
   */
  public void cancel() {
    if( this.mCountDownTimerList.isEmpty() ) {
      this.mDisplayCountDownView.setText("");
      this.mDisplayCountDownView.append("Set time and press start!");
      return;
    }

    IntervalTimer timer = this.mCountDownTimerList.get(this.mCurrentIndex);
    timer.cancel();

    this.mCountDownTimerList.clear();

    this.mDisplayCountDownView.setText("");
    this.mDisplayCountDownView.append("Set time and press start!");
  }

  /**
   * Sets the next interval and starts whenever previous interval finishes.
   */
  public void onIntervalFinished() {
    this.mCurrentIndex++;

    // copying current index into next interval index for readability within this method
    int nextIntervalIndex = this.mCurrentIndex;
    if( nextIntervalIndex == this.mCountDownTimerList.size() ) {
      // Is the last one
      this.mCountDownTimerList.clear();
      this.mDisplayCountDownView.setText("Done!");

      if( mIntervalProcessedListener != null ) {
        this.mIntervalProcessedListener.onIntervalComplete(IntervalCompleteListener.INTERVAL_FINISHED);
      }
      return;
    }

    if( mIntervalProcessedListener != null ) {
      mIntervalProcessedListener.onIntervalComplete(nextIntervalIndex);
    }

    IntervalTimer nextTime = this.mCountDownTimerList.get(nextIntervalIndex);
    nextTime.start();
  }
}
