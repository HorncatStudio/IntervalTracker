package com.horncatstudio.intervaltracker.app;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * Interval timer process and count downs the interval provided.  At the end of an interval, a "ding" sound occurs.
 */
public class IntervalTimer extends CountDownTimer {


  /**
   * An interface a class can implement and register with an interval timer to know when a countdown is complete.
   */
  public interface IntervalTimerListener {
    public void onIntervalFinished();
  }

  /** Members */
  private TextView mTimeTextView = null;
  private Context mContext = null;
  private IntervalTimerListener mListener;

  /** Constructor */
  public IntervalTimer(long millisInFuture, long countdownInterval, TextView timeTextView, Context context, IntervalTimerListener listener) {
    super(millisInFuture, countdownInterval);
    this.mTimeTextView = timeTextView;
    this.mContext = context;
    this.mListener = listener;
  }

  public long getCurrentTimeInSeconds() {
    String currentTime = this.mTimeTextView.getText().toString();
    String[] currentSplitTime = currentTime.split(":", 0);
    long currentMinute = Long.parseLong(currentSplitTime[0]);
    long currentSeconds= Long.parseLong(currentSplitTime[1]);
    return (currentMinute * 60) + currentSeconds;
  }

  public void clearCurrentTime(TextView currentTime) {
    currentTime.setText("");
    currentTime.append("Set time and press start!");
  }

  @Override
  public void onFinish() {
    final MediaPlayer mPlayer = MediaPlayer.create(mContext, R.raw.coinsound);
    mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
      @Override
      public void onCompletion(MediaPlayer mp) {
        mp.release();
      }
    });
    try {
      mPlayer.start();
    } catch (Exception e) {
      e.printStackTrace();
    }

    this.mListener.onIntervalFinished();
  }

  @Override
  public void onTick(long millisUntilFinished) {
    long minutesPart = (millisUntilFinished / 1000) / 60;
    long secondsPart = (millisUntilFinished / 1000) % 60;

    if(secondsPart < 10) {
      this.mTimeTextView.setText(Long.toString(minutesPart) + ":0"  + Long.toString(secondsPart));
    }
    else {
      this.mTimeTextView.setText(Long.toString(minutesPart) + ":" + Long.toString(secondsPart));
    }
  }
}
