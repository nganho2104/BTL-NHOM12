package vn.edu.tlu.cse.btnhom.btap_android;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

import vn.edu.tlu.cse.btnhom.btap_android.R;
import vn.edu.tlu.cse.btnhom.btap_android.Story;
public class AddEditStoryActivity extends AppCompatActivity {
    private EditText etTitle, etAuthor, etDesc, etImage, etCategory, etChap;
    private Button btnSave;
    private DatabaseReference storyRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_story);

        etTitle = findViewById(R.id.etTitle);
        etAuthor = findViewById(R.id.etAuthor);
        etDesc = findViewById(R.id.etDesc);
        etImage = findViewById(R.id.etImage);
        etCategory = findViewById(R.id.etCategory);
        etChap = findViewById(R.id.etChap);
        btnSave = findViewById(R.id.btnSave);

        storyRef = FirebaseDatabase.getInstance().getReference("stories");

        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString();
            String author = etAuthor.getText().toString();
            String desc = etDesc.getText().toString();
            String image = etImage.getText().toString();
            String category = etCategory.getText().toString();
            String chap = etChap.getText().toString();

            if (TextUtils.isEmpty(title) || TextUtils.isEmpty(author)) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ tiêu đề và tác giả", Toast.LENGTH_SHORT).show();
                return;
            }

            String id = UUID.randomUUID().toString();
            Story s = new Story(id, title, author, desc, image, category, chap);
            storyRef.child(id).setValue(s)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(this, "Đã lưu truyện", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });
    }
}