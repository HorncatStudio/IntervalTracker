package com.example.hcrunning.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.ToggleButton;

public class HcRunningActivity extends Activity {
    private TextView mCurrentTimeTextView;
    private MyCountDownTimer mCountDownTimer;
    private NumberPicker mNumberPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_hc_running);

      this.mCurrentTimeTextView = (TextView) findViewById(R.id.textView);

      this.mCountDownTimer = new MyCountDownTimer(mNumberPicker.getValue() * 1000, 50, mCurrentTimeTextView, this);

      this.mNumberPicker = (NumberPicker) findViewById(R.id.numberPicker);
      this.mNumberPicker.setMinValue(1);
      this.mNumberPicker.setMaxValue(20);
      this.mNumberPicker.setWrapSelectorWheel(false);
      this.mNumberPicker.setValue(10);

//      Button startActivationButton = (Button) findViewById(R.id.start_activation_button);
//      startActivationButton.setOnClickListener(new OnClickListener() {
//        @Override
//        public void onClick(View v) {
//          mCountDownTimer.start();
//        }
//      });

//        Button restartActivationButton = (Button) findViewById(R.id.pauseContinue_ToggleButton);
//        restartActivationButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mCountDownTimer = new MyCountDownTimer(mCountDownTimer.getCurrentTimeInSeconds() * 1000, 1000, mCurrentTimeTextView);
//                mCountDownTimer.start();
//            }
//        });
    }

    public void startAndCancelToggleButton(View view) {
      ToggleButton pauseContinueButtonActivator = (ToggleButton) findViewById(R.id.pauseContinue_ToggleButton);
      boolean on = ((ToggleButton) view).isChecked();
      if (on) {
        //Cancel state
        pauseContinueButtonActivator.setClickable(false);
        this.mCountDownTimer.clearCurrrentTime(this.mCurrentTimeTextView);
      } else {
        //Start state
        pauseContinueButtonActivator.setClickable(true);
        this.mCountDownTimer.start();
      }
    }

    public void pauseAndContinueToggleButton(View view) {
      boolean on = ((ToggleButton) view).isChecked();
        if (on) {
          //Continue state
          this.mCountDownTimer = new MyCountDownTimer(this.mCountDownTimer.getCurrentTimeInSeconds() * 1000, 50, mCurrentTimeTextView, this);
          this.mCountDownTimer.start();
        } else {
          //Pause state
          this.mCountDownTimer.cancel();
        }
      }

}
