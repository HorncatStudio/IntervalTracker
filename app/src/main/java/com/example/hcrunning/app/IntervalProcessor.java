package com.example.hcrunning.app;

import android.content.Context;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shinichi on 2014/07/26.
 */
public class IntervalProcessor implements HCRunningCountDownListener {
  private int mCurrentIndex = 0;
  private ArrayList<HCRunningCountDownTimer> mCountDownTimerList;
  private TextView mDisplayCountDownView;
  private Context mContext;

  public IntervalProcessor (TextView displayCountDownView, Context context ) {
    mCountDownTimerList = new ArrayList<HCRunningCountDownTimer>();
    mDisplayCountDownView = displayCountDownView;
    mContext = context;
  }

  public void setIntervals(List<TimeInterval> timeIntervalList) {
    mCountDownTimerList.clear();

    if( timeIntervalList.isEmpty() )
      return;

    for( TimeInterval interval : timeIntervalList ) {
      mCountDownTimerList.add( new HCRunningCountDownTimer(interval.toMiliseconds(), 50, mDisplayCountDownView, mContext, this ) );
    }
    this.mCurrentIndex = 0;
  }

  void start() {
    if( this.mCountDownTimerList.isEmpty() )
      return;

    HCRunningCountDownTimer firstTimer = this.mCountDownTimerList.get(this.mCurrentIndex);
    firstTimer.start();
  }

  public void pause() {}


  public void onIntervalFinished() {
    //
    this.mCurrentIndex++;

    if( this.mCurrentIndex == this.mCountDownTimerList.size() )
      return;

    HCRunningCountDownTimer nextTime = this.mCountDownTimerList.get(this.mCurrentIndex);
    nextTime.start();
  }
}
