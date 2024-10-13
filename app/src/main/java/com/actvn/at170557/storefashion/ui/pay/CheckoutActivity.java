package com.actvn.at170557.storefashion.ui.pay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log; // Thêm import để sử dụng Log
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.actvn.at170557.storefashion.R;
import com.actvn.at170557.storefashion.databinding.ActivityCheckoutBinding;
import com.actvn.at170557.storefashion.ui.address.Address;
import com.actvn.at170557.storefashion.ui.address.ListAddressActivity;
import com.actvn.at170557.storefashion.ui.main.mycart.CartItem;
import com.actvn.at170557.storefashion.ui.order.orderPreview.OrderPreviewActivity;
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
        binding.layoutPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(CheckoutActivity.this, v);
                popup.getMenuInflater().inflate(R.menu.payment_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int itemId = item.getItemId();

                        if (itemId == R.id.payment_card) {
                            showCardPaymentLayout();
                            binding.tvPaytype.setText("Payment by card");
                            return true;
                        } else if (itemId == R.id.payment_cod) {
                            hideCardPaymentLayout();
                            binding.tvPaytype.setText("Cash on delivery");

                            return true;
                        } else {
                            return false;
                        }
                    }

                });
                popup.show();
            }
        });
        binding.gotoPaynow.setOnClickListener(v -> proceedToOrderPreview());
// TextWatcher cho thẻ tín dụng (Card Number)
        binding.tvCardnumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int after) {
                String cardNumber = charSequence.toString();
                if (cardNumber.length() > 0) {
                    // Loại bỏ khoảng trắng và chia thành các nhóm 4 ký tự
                    String formattedCardNumber = cardNumber.replaceAll("\\s+", "").replaceAll("(.{4})(?!$)", "$1 ");

                    // Kiểm tra nếu giá trị thay đổi trước khi cập nhật lại EditText
                    if (!formattedCardNumber.equals(cardNumber)) {
                        ((EditText) findViewById(R.id.tv_cardnumber)).setText(formattedCardNumber);
                        ((EditText) findViewById(R.id.tv_cardnumber)).setSelection(formattedCardNumber.length()); // Đặt con trỏ vào cuối
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

// TextWatcher cho ngày hết hạn (Exp Date)
        binding.tvExDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int after) {
                String expDate = charSequence.toString();
                if (expDate.length() == 2 && !expDate.contains("/")) {
                    // Định dạng MM/YY
                    String formattedExpDate = expDate.substring(0, 2) + "/" + expDate.substring(2);

                    // Kiểm tra nếu giá trị thay đổi trước khi cập nhật lại EditText
                    if (!formattedExpDate.equals(expDate)) {
                        ((EditText) findViewById(R.id.tv_ex_date)).setText(formattedExpDate);
                        ((EditText) findViewById(R.id.tv_ex_date)).setSelection(formattedExpDate.length());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

// TextWatcher cho CVV
        binding.tvCvv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int after) {
                String cvv = charSequence.toString();
                if (cvv.length() > 3) {
                    // Hạn chế CVV chỉ 3 chữ số (Visa, MasterCard) hoặc 4 chữ số (American Express)
                    String formattedCvv = cvv.substring(0, 4);

                    // Kiểm tra nếu giá trị thay đổi trước khi cập nhật lại EditText
                    if (!formattedCvv.equals(cvv)) {
                        ((EditText) findViewById(R.id.tv_cvv)).setText(formattedCvv);
                        ((EditText) findViewById(R.id.tv_cvv)).setSelection(formattedCvv.length());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
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
                                tv_fullAddress.setText(address.getStreet() + ", " + address.getWard() + ", " + address.getDistrict() + ", " + address.getCity());
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
            tv_fullAddress.setText(selectedAddress.getStreet() + ", " + selectedAddress.getWard() + ", " + selectedAddress.getDistrict() + ", " + selectedAddress.getCity());

            Toast.makeText(this, "Đã chọn địa chỉ: " + selectedAddress.getStreet(), Toast.LENGTH_SHORT).show();
        });

        // Hiển thị dialog
        builder.create().show();
    }

    private void proceedToOrderPreview() {
        // Lấy địa chỉ
        String addressName = ((TextView) findViewById(R.id.tvAddress_name)).getText().toString();
        String fullAddress = ((TextView) findViewById(R.id.tv_fullAddress)).getText().toString();

        // Lấy số điện thoại
        String phoneNumber = ((EditText) findViewById(R.id.tvPhoneNumber)).getText().toString();

        // Lấy thông tin thẻ (nếu thanh toán qua thẻ Visa)
        String cardNumber = ((EditText) findViewById(R.id.tv_cardnumber)).getText().toString();
        String expDate = ((EditText) findViewById(R.id.tv_ex_date)).getText().toString();
        String cvv = ((EditText) findViewById(R.id.tv_cvv)).getText().toString();

        // Lấy thông tin phương thức thanh toán
        String paymentType = binding.tvPaytype.getText().toString();

        // Kiểm tra thông tin đầy đủ
        if (phoneNumber.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
            return;
        }

        // Chuyển qua màn hình OrderPreview
        Intent intent = new Intent(CheckoutActivity.this, OrderPreviewActivity.class);
        intent.putExtra("ADDRESS_NAME", addressName);
        intent.putExtra("FULL_ADDRESS", fullAddress);
        intent.putExtra("PHONE_NUMBER", phoneNumber);
        intent.putExtra("PAYMENT_TYPE", paymentType);

        if (paymentType.equals("Payment by card")) {
            intent.putExtra("CARD_NUMBER", cardNumber);
            intent.putExtra("EXP_DATE", expDate);
            intent.putExtra("CVV", cvv);
        }

        // Truyền danh sách các sản phẩm đã chọn
        ArrayList<CartItem> selectedItems = getIntent().getParcelableArrayListExtra("SELECTED_ITEMS");
        intent.putParcelableArrayListExtra("SELECTED_ITEMS", selectedItems);

        startActivity(intent);
    }


    private void showCardPaymentLayout() {
        EditText cardNumber = findViewById(R.id.tv_cardnumber);
        LinearLayoutCompat cardDetailsLayout = findViewById(R.id.linearLayoutCompat3);

        cardNumber.setVisibility(View.VISIBLE);
        cardDetailsLayout.setVisibility(View.VISIBLE);
    }

    private void hideCardPaymentLayout() {
        EditText cardNumber = findViewById(R.id.tv_cardnumber);
        LinearLayoutCompat cardDetailsLayout = findViewById(R.id.linearLayoutCompat3);

        cardNumber.setVisibility(View.GONE);
        cardDetailsLayout.setVisibility(View.GONE);
    }


    protected void hideNavigationBar() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }
}
