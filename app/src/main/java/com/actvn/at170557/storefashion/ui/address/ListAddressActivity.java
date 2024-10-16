package com.actvn.at170557.storefashion.ui.address;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.actvn.at170557.storefashion.R;
import com.actvn.at170557.storefashion.databinding.ActivityListAddressBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListAddressActivity extends AppCompatActivity {

    private ActivityListAddressBinding binding;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        hideNavigationBar();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        binding.imgBack.setOnClickListener(v -> finish());
        binding.addLocation.setOnClickListener(v -> addLocation());
        setupRecyclerView();
    }

    public void addLocation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_address, null);
        builder.setView(dialogView);

        EditText edtAddressName = dialogView.findViewById(R.id.edtAddressName);
        EditText edtStreet = dialogView.findViewById(R.id.edtStreet);
        EditText edtWard = dialogView.findViewById(R.id.edtWard);
        EditText edtDistrict = dialogView.findViewById(R.id.edtDistrict);
        EditText edtCity = dialogView.findViewById(R.id.edtCity);

        builder.setTitle("Add Address")
                .setPositiveButton("Save", (dialog, which) -> {
                    String addressName = edtAddressName.getText().toString();
                    String street = edtStreet.getText().toString();
                    String ward = edtWard.getText().toString();
                    String district = edtDistrict.getText().toString();
                    String city = edtCity.getText().toString();
                    saveAddressToFirestore(addressName, street, ward, district, city);
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void saveAddressToFirestore(String addressName, String street, String ward, String district, String city) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Address address = new Address(addressName, street, ward, district, city);
        String addressId = db.collection("UserAddresses").document(userId).collection("Addresses").document().getId();

        db.collection("UserAddresses").document(userId)
                .collection("Addresses").document(addressId)
                .set(address)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Address saved", Toast.LENGTH_SHORT).show();
                    // Cập nhật lại danh sách địa chỉ
                    setupRecyclerView();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Save address failed", Toast.LENGTH_SHORT).show();
                });
    }




    protected void hideNavigationBar() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = binding.recAddresss;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Address> addressList = new ArrayList<>();
        AddressAdapter adapter = new AddressAdapter(addressList);
        recyclerView.setAdapter(adapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("UserAddresses").document(userId)
                .collection("Addresses")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            Address address = document.toObject(Address.class);
                            addressList.add(address);
                        }
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Get address failed", Toast.LENGTH_SHORT).show();
                });
    }

}
