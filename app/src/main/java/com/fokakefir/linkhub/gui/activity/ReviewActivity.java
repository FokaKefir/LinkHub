package com.fokakefir.linkhub.gui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.NumberPicker;

import com.fokakefir.linkhub.R;

public class ReviewActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener {

    private NumberPicker picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        this.picker = findViewById(R.id.number_picker);
        this.picker.setMinValue(1);
        this.picker.setMaxValue(10);

        this.picker.setOnValueChangedListener(this);
    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {

    }
}