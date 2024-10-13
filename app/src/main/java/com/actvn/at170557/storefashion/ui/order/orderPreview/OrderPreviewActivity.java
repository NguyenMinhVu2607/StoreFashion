package com.actvn.at170557.storefashion.ui.order.orderPreview;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.actvn.at170557.storefashion.R;
import com.actvn.at170557.storefashion.ui.main.mycart.CartItem;
import com.actvn.at170557.storefashion.ui.order.CartItemAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderPreviewActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_preview);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false); // Disable dismissing the dialog manually

        // Nhận dữ liệu từ CheckoutActivity
        String addressName = getIntent().getStringExtra("ADDRESS_NAME");
        String fullAddress = getIntent().getStringExtra("FULL_ADDRESS");
        String phoneNumber = getIntent().getStringExtra("PHONE_NUMBER");
        String paymentType = getIntent().getStringExtra("PAYMENT_TYPE");
        Log.d("OrderPreviewActivity", "paymentType : " + paymentType);

        // Hiển thị thông tin địa chỉ và phương thức thanh toán
        ((TextView) findViewById(R.id.tvAddressName)).setText(addressName);
        ((TextView) findViewById(R.id.tvFullAddress)).setText(fullAddress);
        ((TextView) findViewById(R.id.tvFullSDT)).setText(phoneNumber);
        ((TextView) findViewById(R.id.tvPaymentType)).setText(paymentType);

        // Nếu thanh toán qua Payment by card, hiển thị thông tin thẻ
        if (paymentType.equals("Payment by card")) {
            String cardNumber = getIntent().getStringExtra("CARD_NUMBER");
            Log.d("OrderPreviewActivity", "cardNumber : " + cardNumber);

            ((TextView) findViewById(R.id.tvCardNumber)).setText(cardNumber);
        } else {
            ((ConstraintLayout) findViewById(R.id.layout_card)).setVisibility(View.GONE);

        }

        // Lấy danh sách sản phẩm đã chọn
        ArrayList<CartItem> selectedItems = getIntent().getParcelableArrayListExtra("SELECTED_ITEMS");

        // Log danh sách sản phẩm đã chọn
        if (selectedItems != null) {
            for (CartItem item : selectedItems) {
                Log.d("OrderPreviewActivity", "Selected item: " + item.getName() + ", Price: " + item.getPrice());
            }
        }

        // Thiết lập RecyclerView để hiển thị các sản phẩm đã chọn
        RecyclerView recyclerView = findViewById(R.id.recyclerViewSelectedItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CartItemAdapter adapter = new CartItemAdapter(selectedItems);
        recyclerView.setAdapter(adapter);

        // Tính tổng tiền
        double totalAmount = calculateTotalAmount(selectedItems);
        ((TextView) findViewById(R.id.tv_total_amount)).setText("$" + totalAmount);
        ((TextView) findViewById(R.id.tv_grand_total)).setText("$" + totalAmount);

        // Khi nhấn nút "Xác nhận", tiến hành xử lý đơn hàng
        findViewById(R.id.payy).setOnClickListener(v -> confirmOrder(paymentType, selectedItems, addressName, fullAddress, phoneNumber, totalAmount));
    }

    // Tính tổng tiền từ danh sách sản phẩm đã chọn
    private double calculateTotalAmount(ArrayList<CartItem> selectedItems) {
        double totalAmount = 0;
        for (CartItem item : selectedItems) {
            // Chuyển đổi giá từ String sang double
            double price = Double.parseDouble(item.getPrice());
            // Chuyển đổi số lượng từ String sang int
            int quantity = Integer.parseInt(item.getQuantity());
            // Tính tổng phụ cho sản phẩm này
            double subtotal = price * quantity;
            // Cộng tổng phụ vào tổng số tiền
            totalAmount += subtotal;
        }
        return totalAmount;
    }

    // Xác nhận đơn hàng với thời gian chờ 2 giây cho thanh toán
    private void confirmOrder(String paymentType, ArrayList<CartItem> selectedItems, String addressName, String fullAddress, String phoneNumber, double totalAmount) {
        if (paymentType.equals("Payment by card")) {
            // Hiển thị ProgressDialog chờ xác nhận thanh toán
            showLoadingDialog("Loading . . . . ");

            // Giả lập thanh toán (2 giây)
            new Handler().postDelayed(() -> {
                dismissLoadingDialog();
                Toast.makeText(OrderPreviewActivity.this, "Thanh toán thành công", Toast.LENGTH_SHORT).show();
                createOrder("Đã thanh toán", selectedItems, addressName, fullAddress, phoneNumber, paymentType, totalAmount);
            }, 2000);

        } else {
            showLoadingDialog("Loading . . . . ");
            // Thanh toán khi nhận hàng
            createOrder("Chờ thanh toán", selectedItems, addressName, fullAddress, phoneNumber, paymentType, totalAmount);
        }
    }

    // Hiển thị hộp thoại chờ
    private void showLoadingDialog(String message) {
        if (!progressDialog.isShowing()) {
            progressDialog.setMessage(message);
            progressDialog.show();
        }
    }

    // Dismiss ProgressDialog if it is showing
    private void dismissLoadingDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
    // Tạo đơn hàng và đẩy lên Firestore
    private void createOrder(String status, ArrayList<CartItem> selectedItems, String addressName, String fullAddress, String phoneNumber, String paymentType, double totalAmount) {
        // Lấy instance của Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Lấy UserID hiện tại từ Firebase Authentication
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Tạo dữ liệu đơn hàng
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("status", status);
        orderData.put("addressName", addressName);
        orderData.put("fullAddress", fullAddress);
        orderData.put("phoneNumber", phoneNumber);
        orderData.put("paymentType", paymentType);
        orderData.put("totalAmount", totalAmount);

        // Tạo danh sách sản phẩm
        ArrayList<Map<String, Object>> itemsList = new ArrayList<>();
        for (CartItem item : selectedItems) {
            Map<String, Object> itemData = new HashMap<>();
            itemData.put("name", item.getName());
            itemData.put("price", item.getPrice());
            itemData.put("quantity", item.getQuantity());
            itemsList.add(itemData);
        }
        orderData.put("items", itemsList);

        // Đẩy dữ liệu lên Firestore
        db.collection("Orders").document(userId).collection("UserOrders").add(orderData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(OrderPreviewActivity.this, "Đơn hàng được tạo thành công", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(OrderPreviewActivity.this, "Lỗi khi tạo đơn hàng: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
