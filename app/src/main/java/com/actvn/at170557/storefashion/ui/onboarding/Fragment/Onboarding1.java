package com.actvn.at170557.storefashion.ui.onboarding.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.actvn.at170557.storefashion.R;
import com.actvn.at170557.storefashion.ui.onboarding.OnboardingActivity;

import me.relex.circleindicator.CircleIndicator3;


public class Onboarding1 extends Fragment {

//    public OnboardingFragment1() {
//        // Required empty public constructor
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_onboarding1, container, false);

        TextView buttonNext = view.findViewById(R.id.NextPage);
        TextView buttonSkip = view.findViewById(R.id.skip);
        CircleIndicator3 circleIndicator3  = view.findViewById(R.id.circleIndicator);
        buttonNext.setOnClickListener(v -> {
            // Chuyển sang trang tiếp theo
            ((OnboardingActivity) getActivity()).nextPage();
        });

        buttonSkip.setOnClickListener(v -> {
            // Bỏ qua (skip) và chuyển đến MainActivity
            ((OnboardingActivity) getActivity()).navigateToMainActivity();
        });

        return view;
    }
}