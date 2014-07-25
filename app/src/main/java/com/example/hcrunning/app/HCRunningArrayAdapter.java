package com.example.hcrunning.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shinichi on 2014/07/24.
 */
public class HCRunningArrayAdapter extends ArrayAdapter<TextView> {
  // The resource ID of a Layout used for instantiating Views
  int resource;
  //ArrayList<TextView> items;

    /**
     * Constructs this adapter
     * @param _context	A context to create the Views in
     * @param _resource	The resource ID of a TextView to use when instantiating Views
     * @param _items	The actual objects we're representing
     */
  public HCRunningArrayAdapter(Context _context, int _resource, ArrayList<TextView> _items) {
    super(_context, _resource, _items);
    resource = _resource;
    //items = _items;
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
    View rowView = inflater.inflate(resource, parent, false);

    TextView timeTextView = (TextView) rowView.findViewById(R.id.time);
    timeTextView.setText(getItem(position).getText().toString());

    return rowView;
  }
}
