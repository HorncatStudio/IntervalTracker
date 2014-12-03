package com.example.hcrunning.app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Angelina on 11/7/2014.
 *
 * TODO: Need a better name for this class
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter{

  public static final CharSequence CREATE_TITLE = "Create";
  public static final CharSequence RUN_TITLE = "Run";

  public static final int CREATE_INDEX = 0;
  public static final int RUN_INDEX = 1;

  ArrayList<Integer> mIntervalsToRun = new ArrayList<Integer>();

  public SectionsPagerAdapter( FragmentManager fragmentManager )
  {
    super(fragmentManager);
  }

  CreateFragment mCreateFragment = null;
  RunFragment mRunFragment = null;

  @Override
  public Fragment getItem(int i) {
    switch(i) {
      case CREATE_INDEX:
        if( null == mCreateFragment ) {
          mCreateFragment = new CreateFragment();
        }
        return mCreateFragment;
      default:
        if( null == mRunFragment ) {
          mRunFragment = new RunFragment();
        }
        return mRunFragment;
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

  public void sendIntervalsToRun( final ArrayList<Integer> intervals )
  {
    if( mRunFragment == null)
      return;

    this.mRunFragment.sendIntervals( intervals );
  }

}
