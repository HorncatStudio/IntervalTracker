package com.horncatstudio.intervaltracker.app;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * ArrayAdapter to display the intervals.
 */
public class IntervalArrayAdapter extends ArrayAdapter<TextView> {
  private List<TimeInterval> mIntervals;
  private Activity mParentActivity;
  private final int CLEAR_HIGHLIGHT_INDEX = -1;
  private int mHighlightedIndex = CLEAR_HIGHLIGHT_INDEX;

  /**
   * Constructs this adapter
   * @param context	A context to create the Views in
   * @param items	The actual objects we're representing
   */
  public IntervalArrayAdapter(Context context, ArrayList<TextView> items, Activity activity) {
    super(context, R.layout.row_layout_for_listview, items);
    mParentActivity = activity;
    mIntervals = new ArrayList<TimeInterval>();
  }

  /**
   * Called when a component using this adapter (like a ListView) needs to get a View
   * that represents an object in the ArrayList.
   *
   * @param position		The position in the ArrayList of the object we want a representation of
   * @param convertView	A view that has already been instantiated but is destined to be garbage collected.
   * 							Use this for recycling Views and faster performance.
   * @param parent		The ViewGroup that the returned view will be a child of.
   */
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
    View rowView = inflater.inflate(R.layout.row_layout_for_listview, parent, false);

    TextView timeTextView = (TextView) rowView.findViewById(R.id.time);
    TimeInterval time = mIntervals.get(position);
    timeTextView.setText( time.toString() );

    if( position == this.mHighlightedIndex ) {
      timeTextView.setBackgroundColor(Color.GRAY);
    }

    return rowView;
  }


  /**
   * Adds an interval to the adapter.
   */
  public void add( long minutes, long seconds ) {
    this.add(new TimeInterval(minutes, seconds) );
  }

  /**
   * Adds an interval to the adapter.
   */
  public void add( TimeInterval interval ) {
    mIntervals.add( interval );

    TextView textView = new TextView( getContext() );
    textView.setText(interval.toString());
    this.add(textView);
  }

  public void remove( final TextView textViewToFemove, final int position ) {
    this.remove(textViewToFemove);
    this.mIntervals.remove(position);
  }

  public void replace( int position, TimeInterval interval ) {
    TextView oldView = this.getItem(position);
    this.remove(oldView);

    this.mIntervals.set( position, interval );
    TextView textView = (TextView) mParentActivity.findViewById(R.id.time);

    textView.setText(interval.toString());
    this.insert(textView, position);
  }

  public List<TimeInterval> getTimes() {
    return mIntervals;
  }

  public ArrayList<Integer> getTimesInMilliseconds()
  {
    ArrayList<Integer> milisecondsList = new ArrayList<Integer>();

    for (TimeInterval interval : mIntervals) {
      milisecondsList.add( new Integer( (int)interval.toMilliseconds() ) );
    }

    return milisecondsList;
  }

  public void setHighlighted( final int index ) {
    mHighlightedIndex = index;
    this.notifyDataSetChanged();
  }

  public void clearHighlight() {
    mHighlightedIndex = CLEAR_HIGHLIGHT_INDEX;
    this.notifyDataSetChanged();
  }

  public void clearListItem() {
    mIntervals.clear();
  }
}
