package com.example.hcrunning.app;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import com.example.android.common.view.SlidingTabLayout;

public class HCRunningActivity extends FragmentActivity {

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
}
