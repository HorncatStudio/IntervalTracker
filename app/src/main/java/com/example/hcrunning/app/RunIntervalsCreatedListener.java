package com.example.hcrunning.app;

import java.util.List;

/**
 * Responsible for enabling activities to communicate with their fragments to notify
 * the activity to display the intervals to run with.  This is typically done within the
 * run fragment.
 */
public interface RunIntervalsCreatedListener {

  public void onRunIntervalsCreated( List<TimeInterval> intervals );
}
