package com.example.hcrunning.app;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import com.example.android.common.view.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

public class HcRunningActivity extends FragmentActivity implements RunIntervalsCreatedListener, CreateTimerListener {

  //! The sections pager responsible for going to the different sections of the app.
  //! \todo rename class
  SectionsPagerAdapter mAppSectionsPagerAdapter;

  /**
   * Displays the main sections of the app one at a time.
   */
  ViewPager mViewPager;


  SlidingTabLayout mSlidingTabLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_hc_running);

    mAppSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

    mViewPager = (ViewPager) findViewById(R.id.pager);
    mViewPager.setAdapter(mAppSectionsPagerAdapter);

    mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
    mSlidingTabLayout.setViewPager(mViewPager);

  }

  @Override
  public void onRunIntervalsCreated( List<TimeInterval> intervals) {
    mAppSectionsPagerAdapter.sendIntervalsToRun(intervals);
    mViewPager.setCurrentItem( mAppSectionsPagerAdapter.RUN_INDEX );
  }

  @Override
  public void createATimer() {
    mViewPager.setCurrentItem( mAppSectionsPagerAdapter.CREATE_INDEX );
  }
}
