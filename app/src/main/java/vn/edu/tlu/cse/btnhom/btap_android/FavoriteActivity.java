package vn.edu.tlu.cse.btnhom.btap_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tlu.cse.btnhom.btap_android.Story;
import androidx.appcompat.widget.Toolbar;

// Trong onCreate()

public class FavoriteActivity extends AppCompatActivity {
    private List<Story> favoriteStories = new ArrayList<>();
    private StoryAdapter adapter;
    private RecyclerView rv;
    private List<String> favoriteIds = new ArrayList<>();
    private String currentRole = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite); // tái sử dụng layout danh sách truyện

        rv = findViewById(R.id.rvStory);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StoryAdapter(this, favoriteStories, true,currentRole); // đang ở tủ truyện
        rv.setAdapter(adapter);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tủ truyện");
        loadFavorites();

        ImageView imgLunaLogo = findViewById(R.id.imgToolbarLogo);
        imgLunaLogo.setOnClickListener(v -> {
            startActivity(new Intent(FavoriteActivity.this, MainActivity.class));
        });
    }

    private void loadFavorites() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference favRef = FirebaseDatabase.getInstance().getReference("favorites").child(userId);

        favRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                favoriteIds.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    favoriteIds.add(child.getKey());
                }
                loadStories();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FavoriteActivity.this, "Lỗi tải tủ truyện", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadStories() {
        DatabaseReference storyRef = FirebaseDatabase.getInstance().getReference("stories");
        storyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                favoriteStories.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Story s = child.getValue(Story.class);
                    if (favoriteIds.contains(s.getId())) {
                        favoriteStories.add(s);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FavoriteActivity.this, "Lỗi tải truyện", Toast.LENGTH_SHORT).show();
            }
        });
    }
}