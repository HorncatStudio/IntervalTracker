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
    private HCRunningCountDownTimer mCountDownTimer;
    private ListView mListView;
    private ArrayList<TextView> mArrayList = new ArrayList<TextView>();
    private HCRunningArrayAdapter mAdapter;
    private IntervalProcessor mProcessor;

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

      boolean onStart = ((ToggleButton) view).isChecked();
      if (!onStart) {
        // Action when "Cancel" is pressed
        pauseContinueButtonActivator.setEnabled(false);
//        this.mCountDownTimer.cancel();
//        this.mCountDownTimer.clearCurrentTime(this.mCurrentTimeTextView);
        this.mProcessor.cancel();
        this.mAdapter.clear();
        this.mAdapter.notifyDataSetChanged();
      } else {
        // Action when "Start" is pressed
        pauseContinueButtonActivator.setEnabled(true);
        /* pull list of times to count down
         * foreach one in the list set the time on the count down timer
         * ding at end, then pull do next time
         */
        List<TimeInterval> intervals = mAdapter.getTimes();
        this.mProcessor.setIntervals(intervals);
        this.mProcessor.start();
      }
    }

    public void onPauseAndContinueToggleButton(View view) {
      boolean onPause = ((ToggleButton) view).isChecked();
        if (!onPause) {
          // Action when "Continue" is pressed
          this.mProcessor.start();
        } else {
          // Action when "Pause" is pressed
          this.mProcessor.pause();
        }
      }

    public void showNumberPicker() {
      final Dialog dialog = new Dialog(HcRunningActivity.this);
      dialog.setTitle("Set Timer");
      dialog.setContentView(R.layout.dialog);

      final NumberPicker numberPicker = (NumberPicker) dialog.findViewById(R.id.numberPicker);
      numberPicker.setMaxValue(600);
      numberPicker.setMinValue(1);
      numberPicker.setOnValueChangedListener(this);
      numberPicker.setWrapSelectorWheel(true);

      Button onNumberPickerSetButton = (Button) dialog.findViewById(R.id.buttonSet);
      Button onNumberPickerCancelButton = (Button) dialog.findViewById(R.id.buttonCancel);

      onNumberPickerSetButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          // Separate picked time from number picker into minutes-part and seconds-part
          long minutesPart = numberPicker.getValue() / 60;
          long secondsPart = numberPicker.getValue() % 60;

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
