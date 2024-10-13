package com.actvn.at170557.storefashion.ui.main.setting;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.actvn.at170557.storefashion.R;
import com.actvn.at170557.storefashion.baseapplication.BaseFragment;
import com.actvn.at170557.storefashion.databinding.FragmentSettingsBinding;
import com.actvn.at170557.storefashion.ui.address.ListAddressActivity;
import com.actvn.at170557.storefashion.ui.login_signup.LoginActivity;
import com.actvn.at170557.storefashion.ui.main.mycart.MyCartFragment;
import com.actvn.at170557.storefashion.ui.order.OrderHistoryActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class SettingFragment extends BaseFragment implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 102;
    private FragmentSettingsBinding binding; // Use the generated binding class
    private Context context;
    private MapView mapView;
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    public int getLayoutFragment() {
        return R.layout.fragment_settings;
    }

    @Override
    protected ViewBinding inflateBinding(LayoutInflater inflater, ViewGroup container) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();

        // Khởi tạo FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
// Khởi tạo FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        // Kiểm tra quyền và lấy vị trí
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
        mapView = binding.mapView; // Giả sử bạn đã thêm MapView vào layout
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this); // Thay đổi ở đây
        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(context, "Đã đăng xuất", Toast.LENGTH_SHORT).show();

                // Redirect to login activity after logging out
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish(); // Close the current activity
            }
        });

        binding.layoutAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ListAddressActivity.class);
                startActivity(intent);
            }
        });
        binding.layoutOrderHis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), OrderHistoryActivity.class);
                startActivity(intent);
            }
        });

        binding.layoutContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "MAIL ::", Toast.LENGTH_SHORT).show();
                // Mở ứng dụng email
                try {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                    startActivity(intent);
                } catch (android.content.ActivityNotFoundException e) {
                    Toast.makeText(context, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.layoutMycart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến MyCartFragment
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.viewPager, new MyCartFragment()) // Thay đổi R.id.fragment_container bằng ID thực tế của container fragment
                        .addToBackStack(null) // Thêm transaction vào back stack
                        .commit();
            }
        });

        binding.layoutPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mở trang web example.com
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://example.com"));
                startActivity(browserIntent);
            }
        });

        binding.layoutFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mở trang web example.com
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://example.com"));
                startActivity(browserIntent);
            }
        });
    }

    private void getCurrentLocation() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            // Hiển thị vị trí hoặc sử dụng nó theo cách bạn muốn
                            Log.d("Location", "Latitude: " + latitude + ", Longitude: " + longitude);
                        } else {
                            Log.e("Location", "Không thể lấy vị trí");
                        }
                    }
                });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        // Kiểm tra quyền truy cập vị trí
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Yêu cầu quyền truy cập vị trí
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        // Lấy vị trí hiện tại của người dùng
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            // Hiển thị vị trí của người dùng
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15)); // Đặt mức zoom 15
                            googleMap.addCircle(new CircleOptions()
                                    .center(userLocation)
                                    .radius(100) // Đường kính 100m
                                    .strokeColor(0xFF0000FF) // Màu viền xanh
                                    .fillColor(0x220000FF)); // Màu nền xanh nhạt
                        }
                    }
                });
    }

    // Các phương thức để quản lý MapView
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
