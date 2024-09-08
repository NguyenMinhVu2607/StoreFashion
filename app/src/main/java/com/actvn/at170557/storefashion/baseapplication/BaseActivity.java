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
                    // FCM SDK (and your app) can post notifications.
//                    WorkHelper.scheduleAddLocationWork(this);

                } else {
                    // TODO: Inform user that your app will not show notifications.
                }
            }
    );

    public void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        Log.d("ZZZZ", "askNotificationPermission: ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
//                WorkHelper.scheduleAddLocationWork(this);
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        } else {
//            WorkHelper.scheduleAddLocationWork(this);
        }
    }

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(LocaleHelper.setLocale(newBase));
//    }

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
