package vn.edu.tlu.cse.btnhom.btap_android;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StoryDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_story_detail);

        Story s = (Story) getIntent().getSerializableExtra("story");
        if (s == null) {
            finish();
            return;
        }

        // Hiển thị thông tin truyện
        ImageView img = findViewById(R.id.imgDetail);
        TextView tvTitle = findViewById(R.id.tvDetailTitle);
        TextView tvAuthor = findViewById(R.id.tvDetailAuthor);
        TextView tvDesc = findViewById(R.id.tvDetailDesc);

        tvTitle.setText(s.getTenTruyen());
        tvAuthor.setText("Tác giả: " + s.getTacGia());
        tvDesc.setText(s.getMoTa());
        Glide.with(this).load(s.getLinkAnh()).into(img);

        // Load danh sách chương
        RecyclerView rvChapters = findViewById(R.id.rvChapters);
        rvChapters.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference chapterRef = FirebaseDatabase.getInstance()
                .getReference("chapters")
                .child(s.getId());

        chapterRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> chapterNumbers = new ArrayList<>();
                List<String> chapterLinks = new ArrayList<>();

                for (DataSnapshot child : snapshot.getChildren()) {
                    chapterNumbers.add(child.getKey()); // "1", "2", ...
                    chapterLinks.add(child.getValue(String.class));
                }

                ChapterAdapter adapter = new ChapterAdapter(
                        StoryDetailActivity.this,
                        chapterNumbers,
                        chapterLinks,
                        s.getId() // storyId
                );
                rvChapters.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StoryDetailActivity.this, "Không thể tải chương", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
