package vn.edu.tlu.cse.btnhom.btap_android.readcomic;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tlu.cse.btnhom.btap_android.R;

public class ReadChapterActivity extends AppCompatActivity {
    private RecyclerView rvImages;
    private List<String> imageLinks = new ArrayList<>();
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_chapter);

        int storyId = getIntent().getIntExtra("storyId", -1);
        int chapterIndex = getIntent().getIntExtra("chapterIndex", -1);
        if (storyId == -1 || chapterIndex == -1) {
            Toast.makeText(this, "Thiếu thông tin chương", Toast.LENGTH_SHORT).show();
            finish(); return;
        }

        rvImages = findViewById(R.id.rvImages);
        rvImages.setLayoutManager(new LinearLayoutManager(this));
        imageAdapter = new ImageAdapter(this, imageLinks);
        rvImages.setAdapter(imageAdapter);

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("images").child(String.valueOf(storyId)).child(String.valueOf(chapterIndex));

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                imageLinks.clear();
                for (DataSnapshot img : snapshot.getChildren()) {
                    String url = img.getValue(String.class);
                    if (url != null) imageLinks.add(url);
                }
                imageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ReadChapterActivity.this, "Lỗi tải ảnh", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

