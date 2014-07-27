package com.example.hcrunning.app;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shinichi on 2014/06/13.
 */
public class HCRunningCountDownTimer extends CountDownTimer {
  /** Members */
  private TextView mTimeTextView = null;
  private Context mContext = null;
  //private List<TimeInterval> mIntervals;
  private HCRunningCountDownListener mListener;

  /** Constructor */
  public HCRunningCountDownTimer(long millisInFuture, long countdownInterval, TextView timeTextView, Context context, HCRunningCountDownListener litener) {
    super(millisInFuture, countdownInterval);
    this.mTimeTextView = timeTextView;
    this.mContext = context;
    this.mListener = litener;
    //this.mIntervals = new ArrayList<TimeInterval>();
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
    this.mTimeTextView.setText("Done!");
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
    long minutesPart = millisUntilFinished / 1000 / 60;
    long secondsPart = (millisUntilFinished / 1000 % 60) + 1;
    this.mTimeTextView.setText(Long.toString(minutesPart) + ":" + Long.toString(secondsPart));
  }
}
