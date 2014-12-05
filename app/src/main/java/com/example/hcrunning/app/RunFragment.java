package com.example.hcrunning.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RunFragment extends Fragment implements View.OnClickListener, IntervalCompleteListener {
  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String INTERVAL_PARAMETER = "intervals";

  //! The text view that displays the current interval countdown time in the display
  private TextView mCurrentDisplayTimeTextView;

  //! The interval processes that mangages the intervals being run
  private IntervalProcessor mTimeProcessor;

  //! List of intervals used within the interval processor
  private List<TimeInterval> mIntervals;

  //! The adapter that manages the time intervals in the display for the list view
  private HCRunningArrayAdapter mAdapter;

  //! The list view that displays the intervals while the processor runs
  private ListView mListView;

  private ToggleButton mPauseButton;


  /**
   * Use this factory method to create a new instance of this fragment using the provided parameters.
   *
   * @param intervals The list of intervals in milliseconds that the run fragment is to use for running
   * @return A new instance of fragment RunFragment.
   */
  public static RunFragment newInstance( ArrayList<Integer> intervals )
  {
    RunFragment fragment = new RunFragment();
    Bundle args = new Bundle();
    args.putIntegerArrayList(INTERVAL_PARAMETER, intervals);
    fragment.setArguments(args);
    return fragment;
  }


  public RunFragment() {
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
  }

  @Override
  public void onDetach() {
    super.onDetach();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    this.mTimeProcessor = new IntervalProcessor( this.mCurrentDisplayTimeTextView, getActivity().getApplicationContext() );
    this.mTimeProcessor.registerIntervalCompleteListener( this );
    mAdapter = new HCRunningArrayAdapter(getActivity().getApplicationContext(), new ArrayList<TextView>(), getActivity());

    if (getArguments() != null) {
      ArrayList<Integer> intervals = getArguments().getIntegerArrayList(INTERVAL_PARAMETER);
      for(Integer interval: intervals)
      {
        mAdapter.add( new TimeInterval(interval ));
      }
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_run, container, false);
    this.mCurrentDisplayTimeTextView = (TextView)view.findViewById(R.id.textview_curent_countdown);
    this.mTimeProcessor.setCurrentTimeDisplay( this.mCurrentDisplayTimeTextView );
    this.mListView = (ListView)view.findViewById(R.id.listview_countdown_display);
    mListView.setAdapter(mAdapter);

    ToggleButton startButton = (ToggleButton) view.findViewById(R.id.button_start);
    startButton.setOnClickListener(this);

    mPauseButton = (ToggleButton) view.findViewById(R.id.button_pause);
    mPauseButton.setOnClickListener(this);

    Button resetButton = (Button) view.findViewById(R.id.button_reset);
    resetButton.setOnClickListener(this);

    return view;
  }

  public void sendIntervals( List<TimeInterval> intervals )
  {
    for(TimeInterval interval: intervals)
    {
      mAdapter.add( interval );
    }
  }

  public void onStartAndCancelToggleButton(View view) {
  //! \todo Disabled adding once the clock started ticking.  Make it so they cannot change until it is stopped?
  //  Button addButtonActivator = (Button) view.findViewById(R.id.addButton);

    boolean start = ((ToggleButton) view).isChecked();
    if (!start) {
      // Action when "Cancel" is pressed
      mPauseButton.setEnabled(false);
   //   addButtonActivator.setEnabled(true);
      this.mTimeProcessor.stop();
//        this.mAdapter.clear();
//        this.mAdapter.notifyDataSetChanged();
//        this.mIntervals.clear();         // or this.mAdapter.clearListItem();
    } else {
      // Action when "Start" is pressed
      mPauseButton.setEnabled(true);
   //   addButtonActivator.setEnabled(false);
        /* pull list of times to count down
         * foreach one in the list set the time on the count down timer
         * ding at end, then pull do next time
         */
      this.mIntervals = mAdapter.getTimes();
      this.mTimeProcessor.setIntervals(mIntervals);
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


  public void onResetButton(View view) {
    this.mTimeProcessor.cancel();
    this.mAdapter.clear();
    this.mAdapter.clearListItem();
    this.mAdapter.notifyDataSetChanged();
    //this.mIntervals.clear();
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
      case R.id.button_reset:
        onResetButton(v);
        break;
    }
  }

  public void onIntervalComplete( final int nextIntervalIndex ) {
   this.mAdapter.setHighlighted(nextIntervalIndex);
  }

}
