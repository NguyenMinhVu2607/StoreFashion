package com.actvn.at170557.storefashion.ui.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.actvn.at170557.storefashion.ui.main.favourite.FavouriteFragment;
import com.actvn.at170557.storefashion.ui.main.home.HomeFragment;
import com.actvn.at170557.storefashion.ui.main.mycart.MyCartFragment;
import com.actvn.at170557.storefashion.ui.main.setting.SettingFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new FavouriteFragment();
            case 2:
                return new MyCartFragment();
            case 3:
                return new SettingFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4; // Số lượng trang (4)
    }

    // Thêm các phương thức này để đảm bảo Fragment luôn được tạo lại
    @Override
    public long getItemId(int position) {
        // Trả về vị trí của trang
        return position;
    }

    @Override
    public boolean containsItem(long itemId) {
        // Trả về false để buộc ViewPager2 tạo lại Fragment
        return false;
    }
}
