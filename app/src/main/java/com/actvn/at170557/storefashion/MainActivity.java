package com.actvn.at170557.storefashion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.actvn.at170557.storefashion.baseapplication.BaseActivity;
import com.actvn.at170557.storefashion.databinding.ActivityMainBinding;


public class MainActivity extends BaseActivity {
    private ActivityMainBinding mbinding;

    @Override
    public int getLayoutActivity() {
        return R.layout.activity_main;
    }

    @Override
    protected ViewBinding inflateBinding(LayoutInflater inflater) {
        mbinding = ActivityMainBinding.inflate(inflater);
        return mbinding;
    }
}