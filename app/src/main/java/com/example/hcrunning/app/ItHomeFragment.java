package com.example.hcrunning.app;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class ItHomeFragment extends Fragment implements View.OnClickListener {

  private CreateIntervalsListener mCreateTimersListener = null;

  public ItHomeFragment() {
    // Required empty public constructor
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);

    try {
      mCreateTimersListener = (CreateIntervalsListener) activity;
    } catch (ClassCastException e ) {
      Log.d("Warning", "Activity does not support listener to handle loading the create section.");
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_rt_home, container, false);

    Button createButton = (Button) view.findViewById(R.id.load_create_button);
    createButton.setOnClickListener(this);

    return view;
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.load_create_button:
        onCreateTimer();
        break;
    }
  }

  private void onCreateTimer() {
    if( null != this.mCreateTimersListener ) {
      this.mCreateTimersListener.displayCreateIntervals();
    }
  }
}
