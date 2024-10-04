package com.actvn.at170557.storefashion.ui.login_signup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.actvn.at170557.storefashion.R;
import com.actvn.at170557.storefashion.ui.main.MainActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private boolean isPasswordVisible = false;
    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Setup views
        ImageView togglePasswordView = findViewById(R.id.password_eye_icon);
        emailEditText = findViewById(R.id.email_edittext);
        passwordEditText = findViewById(R.id.password_edittext);
        Button loginButton = findViewById(R.id.login_button);
        TextView signUpTextView = findViewById(R.id.signup_text);

        // Toggle password visibility
        togglePasswordView.setOnClickListener(v -> togglePasswordVisibility());

        // Handle login button click
        loginButton.setOnClickListener(v -> loginUser());

        // Handle sign-up click
        signUpTextView.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            finish();
        });
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Check for potential SQL injection
        if (containsSqlInjectionRisk(email) || containsSqlInjectionRisk(password)) {
            showToast("Thông tin nhập vào không hợp lệ.");
            return;
        }

        if (!isValidEmail(email)) {
            emailEditText.setError("Email không hợp lệ.");
            return;
        }

        if (!isValidPassword(password)) {
            passwordEditText.setError("Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt.");
            return;
        }

        showProgressDialog("Đang đăng nhập...");

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    hideProgressDialog();
                    if (task.isSuccessful()) {
                        navigateToMainActivity();
                    } else {
                        showToast("Đăng nhập thất bại: " + task.getException().getMessage());
                    }
                });
    }

    // Phương thức để phát hiện các ký tự có khả năng gây ra SQL injection
    private boolean containsSqlInjectionRisk(String input) {
        // Danh sách các ký tự có nguy cơ gây SQL injection
        String[] dangerousCharacters = {"'", "\"", ";", "--", "/*", "*/", "#", "="};

        for (String character : dangerousCharacters) {
            if (input.contains(character)) {
                return true;
            }
        }
        return false;
    }


    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        // Password must contain at least one uppercase, one lowercase, one digit, one special character, and be at least 8 characters long
        String passwordPattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[@#$%^&+=!]).{8,}$";
        return !TextUtils.isEmpty(password) && password.matches(passwordPattern);
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            passwordEditText.setInputType(129); // InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
        } else {
            passwordEditText.setInputType(145); // InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        }
        isPasswordVisible = !isPasswordVisible;
        passwordEditText.setSelection(passwordEditText.length());
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showProgressDialog(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
        }
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
