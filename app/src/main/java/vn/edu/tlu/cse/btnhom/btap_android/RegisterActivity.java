package vn.edu.tlu.cse.btnhom.btap_android;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();

        EditText email = findViewById(R.id.etEmail);
        EditText username = findViewById(R.id.etUsername);
        EditText password = findViewById(R.id.etPassword);
        Button btn = findViewById(R.id.btnRegister);

        btn.setOnClickListener(v -> {
            String emailStr = email.getText().toString();
            String passStr = password.getText().toString();

            if (emailStr.isEmpty() || passStr.isEmpty()) {
                Toast.makeText(this, "Không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.createUserWithEmailAndPassword(emailStr, passStr)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(this, "Lỗi: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }
}