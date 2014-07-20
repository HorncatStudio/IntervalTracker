package com.example.hcrunning.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.ToggleButton;

public class HcRunningActivity extends Activity {
    private TextView mCurrentTimeTextView;
    private MyCountDownTimer mCountDownTimer;
    private MyNumberPicker myNumberPicker = new MyNumberPicker();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_hc_running);

      this.mCurrentTimeTextView = (TextView) findViewById(R.id.textView);

      this.mCountDownTimer = new MyCountDownTimer(myNumberPicker.getCurrentValue() * 1000, 50, mCurrentTimeTextView, this);
    }

    public void onAddButton(View view){
        myNumberPicker.showNumberPicker();
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

}
