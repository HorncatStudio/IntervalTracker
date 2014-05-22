package com.example.hcrunning.app;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

public class HcRunningActivity extends Activity {

    public TextView mCurrentTimeTextView;

    //private MyCountDownTimer mCountDownTimer = new MyCountDownTimer(3 * 1000, 50);
    private MyCountDownTimer mCountDownTimer;

    private NumberPicker mNumberPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hc_running);

        mCurrentTimeTextView = (TextView) findViewById(R.id.textView);

        mNumberPicker = (NumberPicker) findViewById(R.id.numberPicker);

        mNumberPicker.setMinValue(1);
        mNumberPicker.setMaxValue(20);
        mNumberPicker.setWrapSelectorWheel(false);
        mNumberPicker.setValue(10);

       // mCountDownTimer = new MyCountDownTimer(Integer.parseInt(mNumberPicker.getDisplayedValues()) * 1000, 50);

        Button startActivationButton = (Button) findViewById(R.id.start_activation_button);
        startActivationButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCountDownTimer = new MyCountDownTimer( mNumberPicker.getValue() * 1000, 50);
                mCountDownTimer.start();
            }
        });

        Button stopActivationButton = (Button) findViewById(R.id.stop_activation_button);
        stopActivationButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCountDownTimer.cancel();
            }
        });

        Button restartActivationButton = (Button) findViewById(R.id.restart_activation_button);
        restartActivationButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentTime = mCurrentTimeTextView.getText().toString();
                String[] currentSplitTime = currentTime.split(":", 0);
                int currentMinute = Integer.parseInt(currentSplitTime[0]);
                int currentSeconds = Integer.parseInt(currentSplitTime[1]);
                int currentTotalInSeconds = (currentMinute * 60) + currentSeconds;
                mCountDownTimer = new MyCountDownTimer( currentTotalInSeconds * 1000, 1000);
                mCountDownTimer.start();
            }
        });

    }

    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long millisInFuture, long countdownInterval) {
            super(millisInFuture, countdownInterval);
        }

        @Override
        public void onFinish() {
            mCurrentTimeTextView.setText("Done!");
            final MediaPlayer mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.coinsound);
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                      mp.release();
                }
            });
            try {
                mPlayer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mCurrentTimeTextView.setText(Long.toString(millisUntilFinished / 1000 / 60) + ":" + Long.toString((millisUntilFinished / 1000 % 60) + 1));
        }
    }

}
