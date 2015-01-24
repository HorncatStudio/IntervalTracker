package com.horncatstudio.intervaltracker.app;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import com.horncatstudio.android.common.view.SlidingTabLayout;

import java.util.List;

/**
 * Main activity for the Interval Tracker app.
 */
public class IntervalTrackerActivity extends FragmentActivity implements RunIntervalsCreatedListener, ItRunIntervalsFragment.IntervalTrainingStateListener {

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
    setContentView(R.layout.activity_interval_tracker);

    mFragmentsAdapter = new IntervalTrackerFragmentAdapter(getSupportFragmentManager());
    mFragmentsAdapter.CREATE_TITLE = getText(R.string.it_create_fragment_title_text);
    mFragmentsAdapter.RUN_TITLE = getText(R.string.it_run_fragment_title_text);

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
  public void onStartIntervalTraining() {
    mFragmentsAdapter.setEditingEnabled(false);
  }

  @Override
  public void onStopIntervalTraining() {
    mFragmentsAdapter.setEditingEnabled(true);
  }
}
