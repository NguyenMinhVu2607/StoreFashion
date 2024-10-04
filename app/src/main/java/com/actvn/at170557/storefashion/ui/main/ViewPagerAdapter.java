package com.actvn.at170557.storefashion.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.actvn.at170557.storefashion.ui.main.favourite.FavouriteFragment;
import com.actvn.at170557.storefashion.ui.main.home.HomeFragment;
import com.actvn.at170557.storefashion.ui.main.mycart.MyCartFragment;
import com.actvn.at170557.storefashion.ui.main.setting.SettingFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private String userId; // Thêm thuộc tính để lưu userId

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, String userId) {
        super(fragmentActivity);
        this.userId = userId; // Khởi tạo userId
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
                MyCartFragment myCartFragment = new MyCartFragment();
                // Đặt userId vào Bundle và truyền cho MyCartFragment
                Bundle bundle = new Bundle();
                bundle.putString("USER_ID", userId);
                myCartFragment.setArguments(bundle);
                return myCartFragment;
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
        return position; // Trả về vị trí của trang
    }

    @Override
    public boolean containsItem(long itemId) {
        return false; // Trả về false để buộc ViewPager2 tạo lại Fragment
    }
}
