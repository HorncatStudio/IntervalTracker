package com.example.hcrunning.app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Angelina on 11/7/2014.
 *
 * TODO: Need a better name for this class
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter{

  public SectionsPagerAdapter( FragmentManager fragmentManager )
  {
    super(fragmentManager);
  }

  @Override
  public Fragment getItem(int i) {
    switch(i) {
      case 0:
        return new CreateFragment();
      default:
        return new RunFragment();
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
      case 0:
        return "Create";
      default:
        return "Run";
    }
  }

}
