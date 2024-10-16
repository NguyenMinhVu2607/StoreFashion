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
import com.actvn.at170557.storefashion.ui.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText, confirmPasswordEditText, userNameEditText, phoneEditText;
    private ImageView togglePasswordView, toggleConfirmPasswordView;
    private Button signUpButton;
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

        userNameEditText = findViewById(R.id.user_name);
        emailEditText = findViewById(R.id.email_edittext);
        phoneEditText = findViewById(R.id.phone_edittext);
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
        String userName = userNameEditText.getText().toString().trim();
        String phoneNumber = phoneEditText.getText().toString().trim();

        // Check for SQL injection in email, password, userName, and phone number
        if (containsSqlInjectionRisk(email) || containsSqlInjectionRisk(password) ||
                containsSqlInjectionRisk(userName) || containsSqlInjectionRisk(phoneNumber)) {
            showToast("Thông tin nhập vào không hợp lệ.");
            return;
        }

        // Kiểm tra tính hợp lệ của email
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email không được để trống.");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Email không hợp lệ.");
            return;
        }

        // Kiểm tra tính hợp lệ của mật khẩu
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

        // Kiểm tra tính hợp lệ của tên người dùng
        if (TextUtils.isEmpty(userName)) {
            showToast("Tên người dùng không được để trống.");
            return;
        }

        if (userName.length() < 3 || userName.length() > 50) {
            userNameEditText.setError("Tên người dùng phải từ 3 đến 50 ký tự.");
            return;
        }

        // Kiểm tra tính hợp lệ của số điện thoại
        if (TextUtils.isEmpty(phoneNumber)) {
            showToast("Số điện thoại không được để trống.");
            return;
        }

        if (!Patterns.PHONE.matcher(phoneNumber).matches() || phoneNumber.length() < 10 || phoneNumber.length() > 15) {
            phoneEditText.setError("Số điện thoại không hợp lệ.");
            return;
        }

        // Tiếp tục xử lý đăng ký tài khoản sau khi các trường đã được kiểm tra
        registerWithFirebase(email, password, userName, phoneNumber);
    }

    // Hàm này để kiểm tra các ký tự nguy hiểm có thể dùng trong tấn công SQL injection hoặc XSS
    private boolean containsSqlInjectionRisk(String input) {
        String[] dangerousCharacters = {"'", "\"", ";", "--", "/*", "*/", "#", "<", ">", "&"};

        for (String character : dangerousCharacters) {
            if (input.contains(character)) {
                return true;
            }
        }
        return false;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Phương thức đăng ký người dùng lên Firebase Authentication và Firestore
    private void registerWithFirebase(String email, String password, String userName, String phoneNumber) {
        ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setMessage("Đang xử lý đăng ký...");
        progressDialog.setCancelable(false); // Người dùng không thể hủy dialog bằng cách nhấn ra ngoài
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userId = user.getUid();
                            user.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> emailTask) {
                                            if (emailTask.isSuccessful()) {
                                                Toast.makeText(SignUpActivity.this, "Account created successfully. Please check your email to verify your account.", Toast.LENGTH_LONG).show();
                                            } else {
                                                Toast.makeText(SignUpActivity.this, "Account creation successful but verification email could not be sent. Please try again later.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                            // Thêm người dùng mới vào Firestore
                            Map<String, Object> userMap = new HashMap<>();
                            userMap.put("userName", userName);
                            userMap.put("phoneNumber", phoneNumber);
                            userMap.put("email", email);

                            db.collection("Users").document(userId)
                                    .set(userMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(SignUpActivity.this, "Account created successfully. Please log in.", Toast.LENGTH_SHORT).show();
                                            navigateToLoginActivity();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(SignUpActivity.this, "Registration successful but error saving data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(SignUpActivity.this, "Đăng ký thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            passwordEditText.setInputType(129);
            togglePasswordView.setImageResource(R.drawable.ic_eye);
        } else {
            passwordEditText.setInputType(145);
            togglePasswordView.setImageResource(R.drawable.ic_eye);
        }
        isPasswordVisible = !isPasswordVisible;
        passwordEditText.setSelection(passwordEditText.length());
    }

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

    public void navigateToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
