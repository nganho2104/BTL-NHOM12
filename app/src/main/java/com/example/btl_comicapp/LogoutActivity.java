package com.example.btl_comicapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LogoutActivity extends AppCompatActivity {

    private EditText etEmail, etUsername, etPassword;
    private CheckBox cbAgree;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logout); // Gán giao diện đăng ký

        // Ánh xạ View
        etEmail = findViewById(R.id.etEmail);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        cbAgree = findViewById(R.id.cbAgree);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String email = etEmail.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Kiểm tra dữ liệu hợp lệ
        if (email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!cbAgree.isChecked()) {
            Toast.makeText(this, "Bạn cần đồng ý với điều khoản!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Giả lập quá trình đăng ký thành công
        Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();

        // Quay lại LoginActivity và gửi dữ liệu username
        Intent intent = new Intent(LogoutActivity.this, LoginActivity.class);
        intent.putExtra("USERNAME", username);
        startActivity(intent);
        finish(); // Kết thúc RegisterActivity
    }
}
