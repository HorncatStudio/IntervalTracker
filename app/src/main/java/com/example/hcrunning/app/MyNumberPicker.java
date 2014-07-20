package com.example.hcrunning.app;

import android.app.Dialog;
import android.util.Log;
import android.widget.NumberPicker;
import android.widget.TextView;

/**
 * Created by NinjaHornCat on 7/20/2014.
 */
public class MyNumberPicker extends HcRunningActivity implements NumberPicker.OnValueChangeListener {

    private final Dialog mDialog = new Dialog(getApplicationContext());
    private final NumberPicker mNumberPicker = (NumberPicker) mDialog.findViewById(R.id.numberPicker);

    private TextView mTimeTextView = null;

    @Override
    public void onValueChange (NumberPicker picker, int oldVal, int newVal) {
        Log.i("Value is ", "" + newVal);
    }

    public int getCurrentValue() {
        return mNumberPicker.getValue();
    }

    public void showNumberPicker() {
        this.mDialog.setTitle("Time Setting");
        this.mDialog.setContentView(R.layout.dialog);

        this.mNumberPicker.setMaxValue(20);
        this.mNumberPicker.setMinValue(1);
        this.mNumberPicker.setWrapSelectorWheel(false);
        this.mNumberPicker.setOnValueChangedListener(this);

        mDialog.show();

    }

    public void onNumberPickerSetButton() {
        this.mTimeTextView.setText(String.valueOf(mNumberPicker.getValue()));
        this.mDialog.dismiss();
    }

    public void onNumberPickerCancelButton() {
        this.mDialog.dismiss();
    }
}
