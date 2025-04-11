package vn.edu.tlu.cse.btnhom.btap_android;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private StoryAdapter adapter;
    private List<Story> list;
    private FirebaseAuth auth;
    private String currentRole = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        RecyclerView rv = findViewById(R.id.rvStory);
        rv.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();

        adapter = new StoryAdapter(this, list, false, currentRole);
        rv.setAdapter(adapter);

        loadStoriesFromFirebase();

        androidx.appcompat.widget.SearchView sv = findViewById(R.id.searchView);
        sv.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }
            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

        ///  hien thi chuc nag them cho tacgia
        FloatingActionButton fab = findViewById(R.id.fabProfile);
        fab.setVisibility(View.GONE); // Mặc định ẩn

        if (auth.getCurrentUser() != null) {
            String uid = auth.getCurrentUser().getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(uid);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    currentRole = snapshot.child("role").getValue(String.class);
                    if ("tacgia".equals(currentRole)) {
                        fab.setVisibility(View.VISIBLE);
                        fab.setOnClickListener(v -> {
                            startActivity(new Intent(MainActivity.this, AddEditStoryActivity.class));
                        });
                    }

                    adapter = new StoryAdapter(MainActivity.this, list, false, currentRole);
                    ((RecyclerView) findViewById(R.id.rvStory)).setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        } else {
            adapter = new StoryAdapter(this, list, false, null);
            ((RecyclerView) findViewById(R.id.rvStory)).setAdapter(adapter);
        }


        ImageView imgAvatar = findViewById(R.id.imgAvatar);
        TextView tvLogin = findViewById(R.id.tvLogin);
        imgAvatar.setOnClickListener(v -> {
            if (auth.getCurrentUser() == null) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            } else {
                String[] options = { "Tủ truyện", "Đăng xuất"};
                new AlertDialog.Builder(this)
                        .setTitle("Tùy chọn")
                        .setItems(options, (dialog, which) -> {
                            if (which == 0) {
                                startActivity(new Intent(MainActivity.this, FavoriteActivity.class));
                            } else if (which == 1) {
                                auth.signOut();
                                Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(this, LoginActivity.class));
                                finish();
                            }
                        })
                        .show();
            }
        });

        if (auth.getCurrentUser() != null) {
            imgAvatar.setVisibility(View.VISIBLE);
            tvLogin.setVisibility(View.GONE);
            Glide.with(this).load("https://i.imgur.com/Conan.jpg")
                    .circleCrop().into(imgAvatar);
        } else {
            imgAvatar.setVisibility(View.GONE);
            tvLogin.setVisibility(View.VISIBLE);
            tvLogin.setOnClickListener(v -> {
                startActivity(new Intent(this, LoginActivity.class));
            });
        }
    }

    private void loadStoriesFromFirebase() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("stories");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Story s = child.getValue(Story.class);
                    list.add(s);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filter(String text) {
        List<Story> filtered = new ArrayList<>();
        for (Story s : list) {
            if (s.getTenTruyen() != null && s.getTenTruyen().toLowerCase().contains(text.toLowerCase())) {
                filtered.add(s);
            }
        }
        adapter = new StoryAdapter(this, filtered, false,currentRole);
        ((RecyclerView) findViewById(R.id.rvStory)).setAdapter(adapter);
    }
}
