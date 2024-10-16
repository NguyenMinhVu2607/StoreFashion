package com.actvn.at170557.storefashion.baseapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewbinding.ViewBinding;


public abstract class BaseActivity<B extends ViewBinding> extends AppCompatActivity {

    protected B mBinding;

    public abstract @LayoutRes int getLayoutActivity();

    public void initViews() {
    }

    public void observerData() {
    }

    public void onClickViews() {
    }

    public void onResizeViews() {
    }

    public void requestWindow() {
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {

                } else {

                }
            }
    );

    public void askNotificationPermission() {
        Log.d("ZZZZ", "askNotificationPermission: ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        } else {
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindow();
        mBinding = inflateBinding(getLayoutInflater());
        setContentView(mBinding.getRoot());
        hideNavigationBar();
        initViews();
        onResizeViews();
        onClickViews();
        observerData();
    }


    protected abstract B inflateBinding(LayoutInflater inflater);

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
    }

    protected void hideNavigationBar() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }
}
