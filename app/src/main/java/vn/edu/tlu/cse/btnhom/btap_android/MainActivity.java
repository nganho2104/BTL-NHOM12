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

import vn.edu.tlu.cse.btnhom.btap_android.StoryAdapter;
import vn.edu.tlu.cse.btnhom.btap_android.Story;
import vn.edu.tlu.cse.btnhom.btap_android.AddEditStoryActivity;
import vn.edu.tlu.cse.btnhom.btap_android.LoginActivity;

public class MainActivity extends AppCompatActivity {
    private StoryAdapter adapter;
    private List<Story> list;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        RecyclerView rv = findViewById(R.id.rvStory);
        rv.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new StoryAdapter(this, list);
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

        FloatingActionButton fab = findViewById(R.id.fabProfile);
        fab.setOnClickListener(v -> {
            if (auth.getCurrentUser() == null) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            } else {
                startActivity(new Intent(MainActivity.this, AddEditStoryActivity.class));
            }
        });

        ImageView imgAvatar = findViewById(R.id.imgAvatar);
        TextView tvLogin = findViewById(R.id.tvLogin);
        imgAvatar.setOnClickListener(v -> {
            if (auth.getCurrentUser() == null) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            } else {
                // Hiện dialog chọn chức năng
                String[] options = {"Thêm truyện","Tủ truyện", "Đăng xuất"};
                new AlertDialog.Builder(this)
                        .setTitle("Tùy chọn")
                        .setItems(options, (dialog, which) -> {
                            if (which == 0) {
                                startActivity(new Intent(MainActivity.this, AddEditStoryActivity.class));
                            } else if (which == 1) {
                                startActivity(new Intent(MainActivity.this, FavoriteActivity.class));
                            } else if (which == 2) {
                                auth.signOut();
                                Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
                                recreate();
                            }
                        })
                        .show();
            }
        });
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            // Đã đăng nhập
            imgAvatar.setVisibility(View.VISIBLE);
            tvLogin.setVisibility(View.GONE);
            Glide.with(this).load("https://i.imgur.com/Conan.jpg") // hoặc ảnh thật
                    .circleCrop().into(imgAvatar);
        } else {
            // Chưa đăng nhập
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
            if (s.getTenTruyen().toLowerCase().contains(text.toLowerCase())) filtered.add(s);
        }
        adapter = new StoryAdapter(this, filtered);
        ((RecyclerView) findViewById(R.id.rvStory)).setAdapter(adapter);
    }
}
