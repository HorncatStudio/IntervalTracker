package com.horncatstudio.intervaltracker.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.test.InstrumentationTestRunner;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;


/**
 * Responsible for processing the list of intervals to run.  Once each interval is completed in counting
 * down in time, an audible "ding" occurs.
 *
 * {@link Fragment}
 */
public class ItRunIntervalsFragment extends Fragment implements View.OnClickListener, IntervalProcessor.IntervalCompleteListener {
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String INTERVAL_PARAMETER = "intervals";

  //! The text view that displays the current interval countdown time in the display
  private TextView mCurrentDisplayTimeTextView;

  //! The interval processes that manages the intervals being run
  private IntervalProcessor mTimeProcessor = null;

  //! The adapter that manages the time intervals in the display for the list view
  private IntervalArrayAdapter mAdapter;

  //! The list view that displays the intervals while the processor runs
  private ListView mListView;

  private ToggleButton mPauseContinueButton;
  private ToggleButton mStartCancelButton;
  private Button mClearButton;

  public interface IntervalTrainingStateListener {
    public void onStartIntervalTraining();
    public void onStopIntervalTraining();
  }

  private IntervalTrainingStateListener mIntervalTrainingStateListner = null;


  /**
   * Use this factory method to create a new instance of this fragment using the provided parameters.
   *
   * @param intervals The list of intervals in milliseconds that the run fragment is to use for running
   * @return A new instance of fragment RunFragment.
   */
  public static ItRunIntervalsFragment newInstance( ArrayList<Integer> intervals ) {
    ItRunIntervalsFragment fragment = new ItRunIntervalsFragment();
    Bundle args = new Bundle();
    args.putIntegerArrayList(INTERVAL_PARAMETER, intervals);
    fragment.setArguments(args);
    return fragment;
  }


  public ItRunIntervalsFragment() {
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);

    try {
      mIntervalTrainingStateListner = (IntervalTrainingStateListener) activity;
    } catch (ClassCastException e) {
      Log.d("Warning", "Activity does not implement listener to handle training state.");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mAdapter = new IntervalArrayAdapter(getActivity().getApplicationContext(), new ArrayList<TextView>(), getActivity());

    if (getArguments() != null) {
      ArrayList<Integer> intervals = getArguments().getIntegerArrayList(INTERVAL_PARAMETER);
      for(Integer interval: intervals) {
        mAdapter.add( new TimeInterval(interval ));
      }
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_run, container, false);
    this.mCurrentDisplayTimeTextView = (TextView)view.findViewById(R.id.textview_curent_countdown);
    this.mListView = (ListView)view.findViewById(R.id.listview_countdown_display);
    mListView.setAdapter(mAdapter);

    mStartCancelButton = (ToggleButton) view.findViewById(R.id.button_start);
    mStartCancelButton.setOnClickListener(this);

    mPauseContinueButton = (ToggleButton) view.findViewById(R.id.button_pause);
    mPauseContinueButton.setOnClickListener(this);

    mClearButton = (Button) view.findViewById(R.id.button_clear);
    mClearButton.setOnClickListener(this);

    // Creating after gui elements are initialized.  The processor relies on a text view in order to update it properly
    //! future refactor - potential refactoring later to remove dependency
    this.mTimeProcessor = new IntervalProcessor( this.mCurrentDisplayTimeTextView, getActivity().getApplicationContext() );
    this.mTimeProcessor.registerIntervalCompleteListener( this );

    return view;
  }

  public void sendIntervals( List<TimeInterval> intervals )  {
    mAdapter.clear();
    mAdapter.clearListItem();
    mAdapter.clearHighlight();

    for(TimeInterval interval: intervals) {
      mAdapter.add( interval );
    }
  }

  public void onStartAndCancelToggleButton(View view) {
  //! \todo Disabled adding once the clock started ticking.  Make it so they cannot change until it is stopped?

    boolean start = ((ToggleButton) view).isChecked();
    if (!start) {
      // Action when "Cancel" is pressed
      mPauseContinueButton.setEnabled(false);
      mPauseContinueButton.setChecked(false);
      this.mTimeProcessor.stop();
      this.mAdapter.clearHighlight();
      clearNotification();
      mIntervalTrainingStateListner.onStopIntervalTraining();
    } else {
      // Action when "Start" is pressed

      if ( this.mAdapter.isEmpty() ) {
        // Do no actions and reset button since there is nothing to start
        this.mStartCancelButton.setChecked(false);
        return;
      }

      sendStartNotification();
      mIntervalTrainingStateListner.onStartIntervalTraining();

      mPauseContinueButton.setEnabled(true);
      this.mTimeProcessor.setIntervals( mAdapter.getTimes() );
      this.mTimeProcessor.start();
    }
  }

  public void onPauseAndContinueToggleButton(View view) {
    boolean pause = ((ToggleButton) view).isChecked();
    if (!pause) {
      // Action when "Continue" is pressed
      this.mTimeProcessor.start();
    } else {
      // Action when "Pause" is pressed
      this.mTimeProcessor.pause();
    }
  }


  public void onClearButton(View view) {
    clearNotification();
    this.mTimeProcessor.cancelAndClear();

    //! todo - Make this into one action
    this.mAdapter.clear();
    this.mAdapter.clearListItem();

    this.mAdapter.notifyDataSetChanged();
    this.mStartCancelButton.setChecked(false);
    this.mPauseContinueButton.setChecked(false);
    this.mPauseContinueButton.setEnabled(false);

    mIntervalTrainingStateListner.onStopIntervalTraining();
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.button_start:
        onStartAndCancelToggleButton(v);
        break;
      case R.id.button_pause:
        onPauseAndContinueToggleButton(v);
        break;
      case R.id.button_clear:
        onClearButton(v);
        break;
    }
  }

  public void onIntervalComplete(final int nextIntervalIndex) {
    this.mAdapter.setHighlighted(nextIntervalIndex);
  }

  public void onIntervalProcessingFinished() {
    sendFinishedNotification();
    displayDoneDialog();
    mPauseContinueButton.setEnabled(false);
    mStartCancelButton.setChecked(false);
    this.mAdapter.clearHighlight();
    mIntervalTrainingStateListner.onStopIntervalTraining();
  }

  public void onFirstIntervalProcessing() {
    this.mAdapter.setHighlighted(0);
  }


  private void displayDoneDialog() {
    Activity parent = this.getActivity();
    AlertDialog.Builder builder = new AlertDialog.Builder(parent);
    builder.setCancelable(false)
        .setMessage(parent.getText(R.string.it_done_text))
        .setPositiveButton(parent.getText(R.string.it_ok_text), new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
      }
    });
    AlertDialog dialog = builder.create();
    dialog.show();
  }

  private void sendStartNotification()
  {
    Activity parent = this.getActivity();

    Intent intent = new Intent(parent, IntervalTrackerActivity.class)
          .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
    PendingIntent pendingIntent = PendingIntent.getActivity(parent, 0, intent, 0);

    Notification notification = new Notification.Builder(parent)
            .setContentTitle("IT!")
            .setContentText(parent.getText(R.string.it_start_notfi_text))
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_launcher).build();
    NotificationManager notificationManager = (NotificationManager) parent.getSystemService(Context.NOTIFICATION_SERVICE);

    notificationManager.notify(0, notification);
  }

  private void sendFinishedNotification()
  {
    Activity parent = this.getActivity();

    Intent intent = new Intent(parent, IntervalTrackerActivity.class)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
    PendingIntent pendingIntent = PendingIntent.getActivity(parent, 0, intent, 0);

    Notification notification = new Notification.Builder(parent)
            .setContentTitle("IT!")
            .setContentText(parent.getText(R.string.it_finished_notfi_text))
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_launcher).build();
    NotificationManager notificationManager = (NotificationManager) parent.getSystemService(Context.NOTIFICATION_SERVICE);

    notificationManager.notify(0, notification);
  }

  private void clearNotification()
  {
    NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
    notificationManager.cancelAll();
  }
}
