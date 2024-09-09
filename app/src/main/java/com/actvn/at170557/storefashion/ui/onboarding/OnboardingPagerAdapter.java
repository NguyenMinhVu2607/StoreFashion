package com.actvn.at170557.storefashion.ui.onboarding;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.actvn.at170557.storefashion.ui.onboarding.Fragment.Onboarding1;
import com.actvn.at170557.storefashion.ui.onboarding.Fragment.Onboarding2;
import com.actvn.at170557.storefashion.ui.onboarding.Fragment.Onboarding3;

import java.util.ArrayList;
import java.util.List;

public class OnboardingPagerAdapter extends FragmentStateAdapter {

    public OnboardingPagerAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new Onboarding1();
            case 1:
                return new Onboarding2();
            case 2:
                return new Onboarding3();
            default:
                return new Onboarding1();
        }
    }

    @Override
    public int getItemCount() {
        return 3; // Số lượng Fragment trong ViewPager
    }
}
