package vn.edu.tlu.cse.btnhom.btap_android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");

        EditText email = findViewById(R.id.etEmail);
        EditText password = findViewById(R.id.etPassword);
        Button btn = findViewById(R.id.btnRegister);

        btn.setOnClickListener(v -> {
            String emailStr = email.getText().toString().trim();
            String passStr = password.getText().toString().trim();

            if (emailStr.isEmpty() || passStr.isEmpty()) {
                Toast.makeText(this, "Không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.createUserWithEmailAndPassword(emailStr, passStr)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String uid = auth.getCurrentUser().getUid();

                            Map<String, Object> userMap = new HashMap<>();
                            userMap.put("email", emailStr);
                            userMap.put("role", "tacgia");

                            userRef.child(uid).setValue(userMap)
                                    .addOnCompleteListener(refTask -> {
                                        if (refTask.isSuccessful()) {
                                            Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                            finish();
                                        } else {
                                            Toast.makeText(RegisterActivity.this, "Lỗi lưu thông tin người dùng", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Đăng ký thất bại: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }
}