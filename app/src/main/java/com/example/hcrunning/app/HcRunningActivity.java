package com.example.hcrunning.app;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class HcRunningActivity extends Activity {

    public TextView mTextView;
    //public MediaPlayer mPlayer;

    private MyCountDownTimer countDownTimer = new MyCountDownTimer(3 * 1000, 50);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hc_running);

        mTextView = (TextView) findViewById(R.id.textView);
        //mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.coinsound);
        /*try {
            mPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        /*mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                try {
                    mp.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });*/

        Button startActivationButton = (Button) findViewById(R.id.start_activation_button);
        startActivationButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.start();
            }
        });

        Button stopActivationButton = (Button) findViewById(R.id.stop_activation_button);
        stopActivationButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.cancel();
            }
        });

        Button restartActivationButton = (Button) findViewById(R.id.restart_activation_button);
        restartActivationButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String time = mTextView.getText().toString();
                String[] time2 = time.split(":", 0);
                countDownTimer = new MyCountDownTimer((Integer.parseInt(time2[0]) * 60 + Integer.parseInt(time2[1])) * 1000, 1000);
                countDownTimer.start();
            }
        });

    }

  /*  @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            mPlayer.release();
        }
    }*/

    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long millisInFuture, long countdownInterval) {
            super(millisInFuture, countdownInterval);
        }

        @Override
        public void onFinish() {
            mTextView.setText("Done!");
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
            mTextView.setText(Long.toString(millisUntilFinished/1000/60) + ":" + Long.toString((millisUntilFinished/1000%60)+1));
        }
    }

}
