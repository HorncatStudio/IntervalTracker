package com.example.hcrunning.app;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.ToggleButton;

public class HcRunningActivity extends Activity implements NumberPicker.OnValueChangeListener{
    private TextView mCurrentTimeTextView;
    private MyCountDownTimer mCountDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_hc_running);

      this.mCurrentTimeTextView = (TextView) findViewById(R.id.textView);
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

      boolean on = ((ToggleButton) view).isChecked();
      if (!on) {
        //action when "Cancel" is pressed
        pauseContinueButtonActivator.setEnabled(false);
        this.mCountDownTimer.cancel();
        this.mCountDownTimer.clearCurrentTime(this.mCurrentTimeTextView);
      } else {
        //action when "Start" is pressed
        pauseContinueButtonActivator.setEnabled(true);
        this.mCountDownTimer.start();
      }
    }

    public void onPauseAndContinueToggleButton(View view) {
      boolean on = ((ToggleButton) view).isChecked();
        if (!on) {
          //action when "Continue" is pressed
          this.mCountDownTimer = new MyCountDownTimer(this.mCountDownTimer.getCurrentTimeInSeconds() * 1000, 50, mCurrentTimeTextView, this);
          this.mCountDownTimer.start();
        } else {
          //action when "Pause" is pressed
          this.mCountDownTimer.cancel();
        }
      }

    public void showNumberPicker() {
      final Dialog dialog = new Dialog(HcRunningActivity.this);
      dialog.setTitle("Set Timer");
      dialog.setContentView(R.layout.dialog);

      final NumberPicker numberPicker = (NumberPicker) dialog.findViewById(R.id.numberPicker);
      numberPicker.setMaxValue(20);
      numberPicker.setMinValue(1);
      numberPicker.setOnValueChangedListener(this);
      numberPicker.setWrapSelectorWheel(false);

      Button onNumberPickerSetButton = (Button) dialog.findViewById(R.id.buttonSet);
      Button onNumberPickerCancelButton = (Button) dialog.findViewById(R.id.buttonCancel);

      onNumberPickerSetButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          mCountDownTimer = new MyCountDownTimer(numberPicker.getValue() * 1000, 50, mCurrentTimeTextView, getApplicationContext());

          long minutesPart = numberPicker.getValue() / 60;
          long secondsPart = numberPicker.getValue() % 60;
          mCurrentTimeTextView.setText(Long.toString(minutesPart) + ":" + Long.toString(secondsPart));

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
