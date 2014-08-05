package com.example.hcrunning.app;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

public class HcRunningActivity extends Activity implements NumberPicker.OnValueChangeListener{
    /** Members */
    private TextView mCurrentTimeTextView;
    private ListView mListView;
    private ArrayList<TextView> mArrayList = new ArrayList<TextView>();
    private HCRunningArrayAdapter mAdapter;
    private IntervalProcessor mProcessor;
    private List<TimeInterval> mIntervals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_hc_running);

      mListView = (ListView) findViewById(R.id.list);
      this.mCurrentTimeTextView = (TextView) findViewById(R.id.textView);
      this.mProcessor = new IntervalProcessor(this.mCurrentTimeTextView, getApplicationContext());

      mAdapter = new HCRunningArrayAdapter(this, mArrayList, this);
      mListView.setAdapter(mAdapter);
    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
      Log.i("Value is ", "" + newVal);
    }

    public void onAddButton(View view){
      this.showNumberPicker();
    }

    public void onStartAndCancelToggleButton(View view) {
      ToggleButton pauseContinueButtonActivator = (ToggleButton) findViewById(R.id.pauseContinueToggleButton);
      Button addButtonActivator = (Button) findViewById(R.id.addButton);

      boolean start = ((ToggleButton) view).isChecked();
      if (!start) {
        // Action when "Cancel" is pressed
        pauseContinueButtonActivator.setEnabled(false);
        addButtonActivator.setEnabled(true);
        this.mProcessor.stop();
//        this.mAdapter.clear();
//        this.mAdapter.notifyDataSetChanged();
//        this.mIntervals.clear();         // or this.mAdapter.clearListItem();
      } else {
        // Action when "Start" is pressed
        pauseContinueButtonActivator.setEnabled(true);
        addButtonActivator.setEnabled(false);
        /* pull list of times to count down
         * foreach one in the list set the time on the count down timer
         * ding at end, then pull do next time
         */
        this.mIntervals = mAdapter.getTimes();
        this.mProcessor.setIntervals(mIntervals);
        this.mProcessor.start();
      }
    }

    public void onPauseAndContinueToggleButton(View view) {
      boolean pause = ((ToggleButton) view).isChecked();
        if (!pause) {
          // Action when "Continue" is pressed
          this.mProcessor.start();
        } else {
          // Action when "Pause" is pressed
          this.mProcessor.pause();
        }
      }

    public void onResetButton(View view) {
      this.mProcessor.cancel();
      this.mAdapter.clear();
      this.mAdapter.notifyDataSetChanged();
      this.mIntervals.clear();         // or this.mAdapter.clearListItem();
    }

    public void showNumberPicker() {
      final Dialog dialog = new Dialog(HcRunningActivity.this);
      dialog.setTitle("Set Timer");
      dialog.setContentView(R.layout.dialog);

      final NumberPicker numberPickerForMinutes = (NumberPicker) dialog.findViewById(R.id.numberPickerMinutes);
      numberPickerForMinutes.setMaxValue(59);
      numberPickerForMinutes.setMinValue(0);
      numberPickerForMinutes.setOnValueChangedListener(this);
      numberPickerForMinutes.setWrapSelectorWheel(true);

      final NumberPicker numberPickerForSeconds = (NumberPicker) dialog.findViewById(R.id.numberPickerSeconds);
      numberPickerForSeconds.setMaxValue(55);
      numberPickerForSeconds.setMinValue(0);
      numberPickerForSeconds.setOnValueChangedListener(this);
      numberPickerForSeconds.setWrapSelectorWheel(true);
      // Setting 5 seconds incrementation below
      String[] stringArray = new String[11];  // 5 * 11 = 55
      for (int i = 0; i < stringArray.length; i++) {
        stringArray[i] = Integer.toString(i * 5);
      }
      numberPickerForSeconds.setDisplayedValues(stringArray);


      Button onNumberPickerSetButton = (Button) dialog.findViewById(R.id.buttonSet);
      Button onNumberPickerCancelButton = (Button) dialog.findViewById(R.id.buttonCancel);

      onNumberPickerSetButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          long minutesPart = numberPickerForMinutes.getValue();
          long secondsPart = numberPickerForSeconds.getValue();

          TimeInterval interval = new TimeInterval(minutesPart, secondsPart);
          mCurrentTimeTextView.setText(interval.toString());

          mAdapter.add(interval);
          dialog.dismiss();
        }
      });

      onNumberPickerCancelButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
         dialog.dismiss();
        }
      });

      dialog.show();
    }

}
