package com.actvn.at170557.storefashion.ui.main.setting;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actvn.at170557.storefashion.R;
import com.actvn.at170557.storefashion.baseapplication.BaseFragment;
import com.actvn.at170557.storefashion.databinding.FragmentHomeBinding;
import com.actvn.at170557.storefashion.databinding.FragmentSettingsBinding;


public class SettingFragment extends BaseFragment {
    private FragmentSettingsBinding binding; // Use the generated binding class
    private Context context;

    @Override
    public int getLayoutFragment() {
        return R.layout.fragment_settings;
    }

    @Override
    protected ViewBinding inflateBinding(LayoutInflater inflater, ViewGroup container) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding;
    }
}
