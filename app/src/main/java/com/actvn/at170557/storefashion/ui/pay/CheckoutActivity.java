package com.actvn.at170557.storefashion.ui.pay;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log; // Thêm import để sử dụng Log
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.actvn.at170557.storefashion.R;
import com.actvn.at170557.storefashion.databinding.ActivityCheckoutBinding;
import com.actvn.at170557.storefashion.ui.address.Address;
import com.actvn.at170557.storefashion.ui.address.ListAddressActivity;
import com.actvn.at170557.storefashion.ui.main.mycart.CartItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    private ActivityCheckoutBinding binding;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        setupRecyclerView();
        List<CartItem> selectedItems = getIntent().getParcelableArrayListExtra("SELECTED_ITEMS");
        hideNavigationBar();
        if (selectedItems != null) {
            Log.d("CheckoutActivity", "Selected Items Count: " + selectedItems.size());
            for (CartItem item : selectedItems) {
                Log.d("CheckoutActivity", "Item: " + item.getName() + ", Price: " + item.getPrice());
            }
        }
        // Lấy số điện thoại của user đã đăng nhập
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // Kiểm tra nếu tài khoản có số điện thoại
            String phoneNumber = currentUser.getPhoneNumber();

            if (phoneNumber != null) {
                // Cập nhật số điện thoại lên TextView
                TextView tvPhoneNumber = findViewById(R.id.tvPhoneNumber); // Thay bằng id của TextView bạn muốn set
                tvPhoneNumber.setText(phoneNumber);
            } else {
                // Trường hợp người dùng không có số điện thoại (ví dụ: đăng nhập bằng email)
                Toast.makeText(this, "Tài khoản không có số điện thoại", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Trường hợp không có user đã đăng nhập
            Toast.makeText(this, "Không có người dùng nào đăng nhập", Toast.LENGTH_SHORT).show();
        }

    }

    private void setupRecyclerView() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Truy cập vào collection địa chỉ của người dùng
        db.collection("UserAddresses").document(userId)
                .collection("Addresses")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Kiểm tra nếu có ít nhất 1 địa chỉ
                        if (!task.getResult().isEmpty()) {
                            // Lấy địa chỉ đầu tiên (vị trí 0)
                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                            Address address = documentSnapshot.toObject(Address.class);

                            // Gán dữ liệu vào TextView
                            if (address != null) {
                                TextView tvAddress_name = findViewById(R.id.tvAddress_name);
                                TextView tv_fullAddress = findViewById(R.id.tv_fullAddress);

                                tvAddress_name.setText(address.getAddressName());
                                tv_fullAddress.setText(address.getStreet()+", "+address.getWard() + ", "+address.getDistrict()+", "+address.getCity());
                            }
                        } else {
                            Toast.makeText(this, "Không có địa chỉ nào", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Không thể tải địa chỉ", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void showAddressDialog(View view) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Lấy tất cả các địa chỉ của user từ Firestore
        db.collection("UserAddresses").document(userId)
                .collection("Addresses")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Address> addressList = new ArrayList<>();

                        for (DocumentSnapshot document : task.getResult()) {
                            Address address = document.toObject(Address.class);
                            addressList.add(address); // Thêm tất cả địa chỉ vào danh sách
                        }

                        // Hiển thị dialog với danh sách địa chỉ
                        showAddressSelectionDialog(addressList);
                    } else {
                        Toast.makeText(this, "Không thể tải địa chỉ", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showAddressSelectionDialog(List<Address> addressList) {
        // Tạo danh sách chuỗi để hiển thị trong Dialog
        List<String> addressDisplayList = new ArrayList<>();
        for (Address address : addressList) {
            addressDisplayList.add(address.getStreet() + ", " + address.getWard() + ", " +
                    address.getDistrict() + ", " + address.getCity());
        }

        // Tạo AlertDialog để hiển thị danh sách địa chỉ
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn địa chỉ");

        // Chuyển danh sách địa chỉ thành mảng chuỗi
        String[] addressesArray = addressDisplayList.toArray(new String[0]);

        builder.setItems(addressesArray, (dialog, which) -> {
            // Khi người dùng chọn một địa chỉ, cập nhật lại TextView
            Address selectedAddress = addressList.get(which);

            // Cập nhật TextView với địa chỉ đã chọn
            TextView tvStreet = findViewById(R.id.tvAddress_name);
            TextView tv_fullAddress = findViewById(R.id.tv_fullAddress);

            tvStreet.setText(selectedAddress.getStreet());
            tv_fullAddress.setText(selectedAddress.getStreet()+", "+selectedAddress.getWard() + ", "+selectedAddress.getDistrict()+", "+selectedAddress.getCity());

            Toast.makeText(this, "Đã chọn địa chỉ: " + selectedAddress.getStreet(), Toast.LENGTH_SHORT).show();
        });

        // Hiển thị dialog
        builder.create().show();
    }


    protected void hideNavigationBar() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }
}
