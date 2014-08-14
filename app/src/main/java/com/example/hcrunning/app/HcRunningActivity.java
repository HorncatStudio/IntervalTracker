package com.example.hcrunning.app;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

public class HCRunningActivity extends Activity {
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
      mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
           TextView oldValue = mAdapter.getItem(position);

           updateFromNumberPicker(oldValue, position);
        }
      });
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
      this.mAdapter.clearListItem();
      this.mAdapter.notifyDataSetChanged();
      //this.mIntervals.clear();
    }

    public void showNumberPicker() {
      final Dialog dialog = createNumberPickerDialog();

      final NumberPicker numberPickerForMinutes = (NumberPicker) dialog.findViewById(R.id.numberPickerMinutes);
      final NumberPicker numberPickerForSeconds = (NumberPicker) dialog.findViewById(R.id.numberPickerSeconds);

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

  public void updateFromNumberPicker( final TextView textViewInterval, final int positionInAdapter ) {
    final Dialog dialog = createNumberPickerDialog();

    TimeInterval interval = HCRunningToolkit.getTimeInterval(textViewInterval);
    final NumberPicker numberPickerForMinutes = (NumberPicker) dialog.findViewById(R.id.numberPickerMinutes);
    numberPickerForMinutes.setValue((int) (interval.Minutes));

    final NumberPicker numberPickerForSeconds = (NumberPicker) dialog.findViewById(R.id.numberPickerSeconds);
    numberPickerForSeconds.setValue((int)(interval.Seconds));

    Button onNumberPickerSetButton = (Button) dialog.findViewById(R.id.buttonSet);
    Button onNumberPickerCancelButton = (Button) dialog.findViewById(R.id.buttonCancel);

    onNumberPickerSetButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        long minutesPart = numberPickerForMinutes.getValue();
        long secondsPart = numberPickerForSeconds.getValue();

        TimeInterval updatedInterval = new TimeInterval(minutesPart, secondsPart);
        updateInterval(updatedInterval, positionInAdapter);
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

  private void updateInterval( TimeInterval newInterval, int position ) {
    this.mAdapter.replace(position, newInterval);
  }

  private Dialog createNumberPickerDialog() {
    final Dialog dialog = new Dialog(HCRunningActivity.this);
    dialog.setTitle("Set Timer");
    dialog.setContentView(R.layout.dialog);

    final NumberPicker numberPickerForMinutes = (NumberPicker) dialog.findViewById(R.id.numberPickerMinutes);
    numberPickerForMinutes.setMaxValue(59);
    numberPickerForMinutes.setMinValue(0);
//    numberPickerForMinutes.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
//      @Override
//      public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//        picker.setValue((newVal < oldVal) ? (oldVal -1) : (oldVal + 1));
//      }
//    });
    numberPickerForMinutes.setWrapSelectorWheel(true);

    final NumberPicker numberPickerForSeconds = (NumberPicker) dialog.findViewById(R.id.numberPickerSeconds);
    //final String[] seconds = new String[12];
    final String[] seconds = {"00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55"};
//    for (int i = 0; i < seconds.length; i++) {
//      String number = Integer.toString(i * 5);
//      //seconds[i] = number.length() < 2 ? "0" + number : number;
//      seconds[i] = number;
//    }
    numberPickerForSeconds.setDisplayedValues(seconds);
    numberPickerForSeconds.setMaxValue(seconds.length - 1);
    numberPickerForSeconds.setMinValue(0);
    numberPickerForSeconds.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
      @Override
      public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        //picker.setValue((newVal < oldVal) ? (oldVal - 5) : (oldVal + 5));
        newVal = Integer.parseInt(seconds[newVal]);
      }
    });
    numberPickerForSeconds.setWrapSelectorWheel(true);

    return dialog;
  }
}
