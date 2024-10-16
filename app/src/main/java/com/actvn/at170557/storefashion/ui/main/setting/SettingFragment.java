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
import com.actvn.at170557.storefashion.ui.main.home.HomeFragment;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SettingFragment extends BaseFragment implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 102;
    private FragmentSettingsBinding binding;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
        mapView = binding.mapView;
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        getUserNameFromFirestore(new SettingFragment.FirestoreCallback() {
            @Override
            public void onCallback(String userName) {
                binding.txtUserName.setText(userName);
            }
        });
        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(context, "Logout successful", Toast.LENGTH_SHORT).show();

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
                        .replace(R.id.viewPager, new MyCartFragment())
                        .addToBackStack(null)
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
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
                            googleMap.addCircle(new CircleOptions()
                                    .center(userLocation)
                                    .radius(100)
                                    .strokeColor(0xFF0000FF)
                                    .fillColor(0x220000FF));
                        }
                    }
                });
    }
    public interface FirestoreCallback {
        void onCallback(String userName);
    }
    public void getUserNameFromFirestore(SettingFragment.FirestoreCallback callback) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null && !currentUser.isEmailVerified()) {
            binding.txtVerify.setText("Not verified");
            binding.txtVerify.setBackgroundColor(getResources().getColor(R.color.red));
        } else {
            binding.txtVerify.setText("Verified");
            binding.txtVerify.setBackgroundColor(getResources().getColor(R.color.color_bar_pre));
        }

        if (currentUser != null) {
            String userId = currentUser.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Users").document(userId).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    String userName = document.getString("userName");

                                    if (userName != null && !userName.isEmpty()) {
                                        callback.onCallback(userName);
                                    } else {
                                        callback.onCallback("User");
                                    }
                                } else {
                                    callback.onCallback("User");
                                }
                            } else {
                                callback.onCallback("User");
                            }
                        }
                    });
        } else {
            callback.onCallback("No User Logged In");
        }
    }

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
