package com.actvn.at170557.storefashion.ui.onboarding.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.actvn.at170557.storefashion.R;
import com.actvn.at170557.storefashion.ui.onboarding.OnboardingActivity;


public class Onboarding3 extends Fragment {

//    public OnboardingFragment3() {
//        // Required empty public constructor
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_onboarding3, container, false);

        TextView buttonFinish = view.findViewById(R.id.NextPage);

        buttonFinish.setOnClickListener(v -> {
            // Chuyển đến MainActivity (hoàn thành onboarding)
            ((OnboardingActivity) getActivity()).navigateToMainActivity();
        });

        return view;
    }
}
