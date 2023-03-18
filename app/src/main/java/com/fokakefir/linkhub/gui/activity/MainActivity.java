package com.fokakefir.linkhub.gui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.fokakefir.linkhub.R;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Fragment> homeFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}