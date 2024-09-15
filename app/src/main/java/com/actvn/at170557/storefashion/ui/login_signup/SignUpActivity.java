package com.actvn.at170557.storefashion.ui.login_signup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.actvn.at170557.storefashion.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText, confirmPasswordEditText;
    private ImageView togglePasswordView, toggleConfirmPasswordView;
    private Button signUpButton;
TextView user_name;
    private boolean isPasswordVisible = false, isConfirmPasswordVisible = false;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        hideNavigationBar();

        // Khởi tạo FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        user_name = findViewById(R.id.user_name);
        emailEditText = findViewById(R.id.email_edittext);
        passwordEditText = findViewById(R.id.password_edittext);
        confirmPasswordEditText = findViewById(R.id.password_confirm);
        togglePasswordView = findViewById(R.id.password_eye_icon);
        toggleConfirmPasswordView = findViewById(R.id.confirm_password_eye_icon);
        signUpButton = findViewById(R.id.signup_button);

        // Xử lý ẩn/hiện mật khẩu khi nhấn vào icon
        togglePasswordView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility();
            }
        });

        toggleConfirmPasswordView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleConfirmPasswordVisibility();
            }
        });

        // Xử lý sự kiện khi nhấn nút Đăng ký
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
        TextView login_text =findViewById(R.id.login_text);
        login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // Phương thức để ẩn thanh điều hướng và status bar
    protected void hideNavigationBar() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }

    // Phương thức kiểm tra mật khẩu có hợp lệ không
    private boolean isPasswordValid(String password) {
        // Mật khẩu phải có ít nhất 6 ký tự, 1 chữ cái viết hoa và 1 ký tự đặc biệt
        Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{6,}$");
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    // Phương thức đăng ký tài khoản
    private void createAccount() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email không được để trống.");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Email không hợp lệ.");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Mật khẩu không được để trống.");
            return;
        }

        if (!isPasswordValid(password)) {
            passwordEditText.setError("Mật khẩu phải có ít nhất 6 ký tự, 1 chữ cái viết hoa và 1 ký tự đặc biệt.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Mật khẩu xác nhận không khớp.");
            return;
        }

        // Tạo tài khoản bằng Firebase Authentication
        ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setMessage("Đang xử lý đăng ký...");
        progressDialog.setCancelable(false); // Người dùng không thể hủy dialog bằng cách nhấn ra ngoài
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Ẩn ProgressDialog sau khi hoàn thành
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            // Lấy user ID sau khi đăng ký thành công
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userId = user.getUid();
                            String userName = user_name.getText().toString().trim();

                            // Kiểm tra tên người dùng
                            if (userName.isEmpty()) {
                                Toast.makeText(SignUpActivity.this, "Tên người dùng không được để trống.", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            // Thêm người dùng mới vào Firestore trong collection "Users" với userId là document ID
                            db.collection("Users").document(userId)
                                    .set(new User(userName))  // Trực tiếp thêm đối tượng User mới với tên
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(SignUpActivity.this, "Tài khoản đã được tạo thành công và lưu trữ dữ liệu người dùng.", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(SignUpActivity.this, "Đăng ký thành công nhưng lỗi khi lưu dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        } else {
                            // Đăng ký thất bại
                            Toast.makeText(SignUpActivity.this, "Đăng ký thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    // Phương thức chuyển đổi giữa hiển thị và ẩn mật khẩu
    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Đặt inputType thành mật khẩu (ẩn)
            passwordEditText.setInputType(129); // InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
            togglePasswordView.setImageResource(R.drawable.ic_eye);
        } else {
            // Đặt inputType thành văn bản thường (hiển thị)
            passwordEditText.setInputType(145); // InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            togglePasswordView.setImageResource(R.drawable.ic_eye);
        }
        isPasswordVisible = !isPasswordVisible;
        // Đưa con trỏ về cuối chuỗi
        passwordEditText.setSelection(passwordEditText.length());
    }

    // Phương thức chuyển đổi giữa hiển thị và ẩn xác nhận mật khẩu
    private void toggleConfirmPasswordVisibility() {
        if (isConfirmPasswordVisible) {
            confirmPasswordEditText.setInputType(129);
            toggleConfirmPasswordView.setImageResource(R.drawable.ic_eye);
        } else {
            confirmPasswordEditText.setInputType(145);
            toggleConfirmPasswordView.setImageResource(R.drawable.ic_eye);
        }
        isConfirmPasswordVisible = !isConfirmPasswordVisible;
        confirmPasswordEditText.setSelection(confirmPasswordEditText.length());
    }

    public void navigateToRESActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
