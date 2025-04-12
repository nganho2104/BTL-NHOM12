package vn.edu.tlu.cse.btnhom.btap_android;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import vn.edu.tlu.cse.btnhom.btap_android.R;
import vn.edu.tlu.cse.btnhom.btap_android.Story;
public class AddEditStoryActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText etTitle, etAuthor, etDesc, etCategory, etChap;
    private Button btnSave, btnChooseImage;
    private ImageView imgPreview; // Hiển thị ảnh được chọn
    private DatabaseReference storyRef;
    private String imagePath; // Lưu đường dẫn ảnh

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_story);

        etTitle = findViewById(R.id.etTitle);
        etAuthor = findViewById(R.id.etAuthor);
        etDesc = findViewById(R.id.etDesc);
        etCategory = findViewById(R.id.etCategory);
        etChap = findViewById(R.id.etChap);
        btnSave = findViewById(R.id.btnSave);
        btnChooseImage = findViewById(R.id.btnChooseImage);
        imgPreview = findViewById(R.id.imgPreview);

        storyRef = FirebaseDatabase.getInstance().getReference("stories");

        btnChooseImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString();
            String author = etAuthor.getText().toString();
            String desc = etDesc.getText().toString();
            String category = etCategory.getText().toString();
            String chap = etChap.getText().toString();

            if (TextUtils.isEmpty(title) || TextUtils.isEmpty(author)) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ tiêu đề và tác giả", Toast.LENGTH_SHORT).show();
                return;
            }

            String id = UUID.randomUUID().toString();
            Story s = new Story(id, title, author, desc, imagePath, category, chap);

            storyRef.child(id).setValue(s)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(this, "Đã lưu truyện", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            imgPreview.setImageURI(imageUri);

            // Lưu ảnh vào bộ nhớ trong và lấy đường dẫn
            saveImageToInternalStorage(imageUri);
        }
    }

    private void saveImageToInternalStorage(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            FileOutputStream outputStream = openFileOutput("story_image.jpg", Context.MODE_PRIVATE);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.close();

            // Lưu đường dẫn ảnh
            imagePath = getFilesDir().getAbsolutePath() + "/story_image.jpg";
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
