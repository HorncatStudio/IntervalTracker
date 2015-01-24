package com.horncatstudio.intervaltracker.app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Angelina on 11/7/2014.
 *
 * TODO: Need a better name for this class
 */
public class IntervalTrackerFragmentAdapter extends FragmentPagerAdapter{

  public static final CharSequence CREATE_TITLE = "Create";
  public static final CharSequence RUN_TITLE = "Run";

  public static final int CREATE_INDEX = 0;
  public static final int RUN_INDEX = 1;

  public IntervalTrackerFragmentAdapter(FragmentManager fragmentManager)
  {
    super(fragmentManager);
  }

  CreateIntervalsFragment mCreateIntervalsFragment = null;
  ItRunIntervalsFragment mItRunIntervalsFragment = null;

  @Override
  public Fragment getItem(int i) {
    switch(i) {
      case CREATE_INDEX:
        if( null == mCreateIntervalsFragment) {
          mCreateIntervalsFragment = new CreateIntervalsFragment();
        }
        return mCreateIntervalsFragment;
      default:
        if( null == mItRunIntervalsFragment) {
          mItRunIntervalsFragment = new ItRunIntervalsFragment();
        }
        return mItRunIntervalsFragment;
    }
  }

  @Override
  public int getCount() {
    return 2;
  }

  @Override
  public CharSequence getPageTitle( int index )
  {
    switch(index) {
      case CREATE_INDEX:
        return CREATE_TITLE;
      case RUN_INDEX:
        return RUN_TITLE;
      default:
        return RUN_TITLE;
    }
  }

  public void sendIntervalsToRun( final List<TimeInterval> intervals ) {
    if( mItRunIntervalsFragment == null )
      return;

    this.mItRunIntervalsFragment.sendIntervals( intervals );
  }

  public void setEditingEnabled( final boolean enabled ) {
    mCreateIntervalsFragment.setEditingEnabled(enabled);
  }

}
