package com.horncatstudio.intervaltracker.app;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Process the intervals provided.  Allows the user to start, stop, pause, and cancelAndClear interface processing.
 */
public class IntervalProcessor implements IntervalTimer.IntervalTimerListener {

  public enum State {
    INITIALIZED,
    PRE_START_COUNTDOWN,
    ACTIVE,
    PAUSED
  }

  /**
   * An internal state manager to help determine the current and previous state of the interval processing.
   */
  private class StateManager {
    private State mProcessingState = State.INITIALIZED;
    private State mPreviousProcessingState = State.INITIALIZED;

    public StateManager() {}

    final State getState() {
      return mProcessingState;
    }

    final State getPreviousState() {
      return mPreviousProcessingState;
    }

    public final boolean isPaused() {
      return ( State.PAUSED == this.mProcessingState );
    }

    void setState( State newState ) {
      this.mPreviousProcessingState = this.mProcessingState;
      this.mProcessingState = newState;
    }
  }

  /**
   * Listener that can be registered with the IntervalProcessor in order to do an action
   * when an individual interval has been processed.
   */
  public interface IntervalCompleteListener {
    public void onIntervalComplete( final int intervalIndex );
    public void onIntervalProcessingFinished();
    public void onFirstIntervalProcessing();
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

  private StateManager mProcessingState;

  private IntervalTimer mPreStartCountdownTimer = null;
  private final IntervalTimer DEFAULT_PRES_START_TIMER;
  private final String INITIALIZED_TIMER_DISPLAY_TEXT = "0:00";

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
    mProcessingState = new StateManager();

    final long countdownTimeInMilliseconds = 5000; // 5 seconds
    DEFAULT_PRES_START_TIMER =  new IntervalTimer(countdownTimeInMilliseconds, 50,
        mDisplayCountDownView, mContext, new IntervalTimer.IntervalTimerListener() {
      @Override
      public void onIntervalFinished() {
        preCountdownTimerListenerCall();
      }
    }) ;

    mPreStartCountdownTimer = DEFAULT_PRES_START_TIMER;
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
    this.mProcessingState.setState(State.INITIALIZED);
  }

  public boolean isEmpty() {
    return mCountDownTimerList.isEmpty();
  }

  /**
   * Starts the timers. If empty it returns without further operation.
   */
  public void start() {
    if( this.isEmpty() )
      return;

    if( this.mProcessingState.isPaused() &&
        this.mProcessingState.getPreviousState() == State.ACTIVE ) {
        internalStart();
        return;
      }

    //! NOTE - Doesn't matter whether it was paused or not, If it was paused
    //         or was just initialized it will need call this method
    beginStartCountdown();
  }

  private void internalStart() {
    this.mProcessingState.setState(State.ACTIVE);

    final State previousState = this.mProcessingState.getPreviousState();
    if( this.isListenerSet() ) {
      if( previousState == State.PAUSED )
        mIntervalProcessedListener.onIntervalComplete(this.mCurrentIndex);
      else if( previousState == State.PRE_START_COUNTDOWN )
        mIntervalProcessedListener.onFirstIntervalProcessing();
    }

    IntervalTimer firstTimer = this.mCountDownTimerList.get(this.mCurrentIndex);
    firstTimer.start();
  }

  private void beginStartCountdown() {
    this.mProcessingState.setState(State.PRE_START_COUNTDOWN);
    this.mPreStartCountdownTimer.start();
  }

  private void resetPreStartCountdownTimer() {
    this.mPreStartCountdownTimer = this.DEFAULT_PRES_START_TIMER;
  }

  /**
   * Stops the timer.  Resets interval to process back to first interval.
   */
  public void stop() {
    if( this.isEmpty() )
      return;

    IntervalTimer stoppedTimer = null;
    State lastDoingSomethingState = null;
    if( this.mProcessingState.isPaused() ) {
      lastDoingSomethingState = this.mProcessingState.getPreviousState();
    } else {
      lastDoingSomethingState = this.mProcessingState.getState();
    }

    if( lastDoingSomethingState == State.ACTIVE ) {
      stoppedTimer = this.mCountDownTimerList.get(this.mCurrentIndex);
    } else if ( lastDoingSomethingState == State.PRE_START_COUNTDOWN ) {
      stoppedTimer = this.mPreStartCountdownTimer;
    }

    stoppedTimer.cancel();

    this.mDisplayCountDownView.setText(INITIALIZED_TIMER_DISPLAY_TEXT);
    this.mCurrentIndex = 0;
    this.mProcessingState.setState(State.INITIALIZED);
    this.resetPreStartCountdownTimer();
  }

  /**
   * Pauses the current interval being processed.  To restart processing the interval paused, use the
   * start() method.  If no timers are present, the state shall remain unchanged.
   */
  public void pause() {
    if( this.mCountDownTimerList.isEmpty() )
      return;

    this.mProcessingState.setState( State.PAUSED );

    IntervalTimer pausedTimer = null;
    if( this.mProcessingState.getPreviousState() == State.ACTIVE ) {
      pausedTimer = this.mCountDownTimerList.get(this.mCurrentIndex);
      pausedTimer.cancel();

      final long totalInMilliSeconds = this.getCurrentDisplayTimeInMilliseconds();
      IntervalTimer replacingTimer = new IntervalTimer(totalInMilliSeconds, 50, mDisplayCountDownView, mContext, this);

      this.mCountDownTimerList.set(this.mCurrentIndex, replacingTimer);
    } else if (this.mProcessingState.getPreviousState() == State.PRE_START_COUNTDOWN ) {
      pausedTimer = this.mPreStartCountdownTimer;
      pausedTimer.cancel();

      final long totalInMilliSeconds = this.getCurrentDisplayTimeInMilliseconds();
      this.mPreStartCountdownTimer = new IntervalTimer( totalInMilliSeconds, 50, this.mDisplayCountDownView,
          this.mContext, new IntervalTimer.IntervalTimerListener() {
        @Override
        public void onIntervalFinished() {
          preCountdownTimerListenerCall();
        }
      });
    }
  }

  private final long getCurrentDisplayTimeInMilliseconds() {
    TimeInterval pausedInterval = IntervalTrackerToolkit.getTimeInterval(this.mDisplayCountDownView);
    return pausedInterval.toMilliseconds();
  }

  /**
   * Clears all the intervals.
   */
  public void cancelAndClear() {
    this.mDisplayCountDownView.setText(INITIALIZED_TIMER_DISPLAY_TEXT);
    this.mProcessingState.setState(State.INITIALIZED);

    if( this.mProcessingState.getPreviousState() == State.PRE_START_COUNTDOWN )
      this.mPreStartCountdownTimer.cancel();
    this.resetPreStartCountdownTimer();

    if( this.isEmpty() ) {
      return;
    }

    IntervalTimer timer = this.mCountDownTimerList.get(this.mCurrentIndex);
    timer.cancel();
    this.mCountDownTimerList.clear();
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
      onIntervalFinish();
      if( this.isListenerSet() ) {
        this.mIntervalProcessedListener.onIntervalProcessingFinished();
      }
      return;
    }

    if( this.isListenerSet() ) {
      mIntervalProcessedListener.onIntervalComplete(nextIntervalIndex);
    }

    IntervalTimer nextTime = this.mCountDownTimerList.get(nextIntervalIndex);
    nextTime.start();
  }

  private void onIntervalFinish() {
    this.mCountDownTimerList.clear();
    this.resetPreStartCountdownTimer();
    this.mDisplayCountDownView.setText(INITIALIZED_TIMER_DISPLAY_TEXT);
    this.mCurrentIndex = 0;
  }

  private boolean isListenerSet() {
    return ( null != this.mIntervalProcessedListener );
  }

  /**
   * A method to be used uniquely during the listener call on the Pre-Countdown Timer
   * to restart counting down to starting the interval processing.
   */
  private void preCountdownTimerListenerCall() {
    resetPreStartCountdownTimer();
    internalStart();
  }

}
