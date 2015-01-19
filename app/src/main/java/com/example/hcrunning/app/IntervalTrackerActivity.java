package com.example.hcrunning.app;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import com.example.android.common.view.SlidingTabLayout;

import java.util.List;

/**
 * Main activity for the Interval Tracker app.
 */
public class IntervalTrackerActivity extends FragmentActivity implements RunIntervalsCreatedListener, CreateIntervalsListener {

  //! The sections pager responsible for going to the different sections of the app.
  //! \todo rename class
  IntervalTrackerFragmentAdapter mFragmentsAdapter;

  //! Manages the fragments to determine which one is currently active in the app
  ViewPager mViewPager;

  //! Tab layout manages the display of each of the fragments within the application
  SlidingTabLayout mSlidingTabLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_hc_running);

    mFragmentsAdapter = new IntervalTrackerFragmentAdapter(getSupportFragmentManager());

    mViewPager = (ViewPager) findViewById(R.id.pager);
    mViewPager.setAdapter(mFragmentsAdapter);

    mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
    mSlidingTabLayout.setViewPager(mViewPager);

  }

  @Override
  public void onRunIntervalsCreated( List<TimeInterval> intervals) {
    mFragmentsAdapter.sendIntervalsToRun(intervals);
    mViewPager.setCurrentItem( mFragmentsAdapter.RUN_INDEX );
  }

  @Override
  public void displayCreateIntervals() {
    mViewPager.setCurrentItem( mFragmentsAdapter.CREATE_INDEX );
  }
}