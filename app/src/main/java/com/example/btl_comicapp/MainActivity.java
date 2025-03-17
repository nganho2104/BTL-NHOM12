package com.example.btl_comicapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.btl_comicapp.Adapter.TruyenTranhAdapter;
import com.example.btl_comicapp.database.DbHelper;
import com.example.btl_comicapp.object.TruyenTranh;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    GridView gdvDSTruyen;
    ArrayList<TruyenTranh> truyenTranhArrayList;
    EditText edtTimKiem;
    private TruyenTranhAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        init();
        anhXa();
        setup();
        setClick();
        loadData();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void init() {
        truyenTranhArrayList = new ArrayList<>();
        // Thêm các mục TruyenTranh vào danh sách

        // Chỉnh sửa khởi tạo adapter
        adapter = new TruyenTranhAdapter(this, R.layout.iteam_truyen, truyenTranhArrayList);
    }

    private void anhXa() {
        gdvDSTruyen = findViewById(R.id.gdvDSTruyen);
        edtTimKiem = findViewById(R.id.edtTimKiem);
    }


    private void setClick() {
        edtTimKiem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String query = edtTimKiem.getText().toString();
                adapter.sortTruyen(query);
            }
        });
    }
    private void setup() {
        gdvDSTruyen.setAdapter(adapter);

        // Thiết lập OnItemClickListener để mở màn hình chi tiết
        adapter.setOnItemClickListener(truyenTranh -> {
            Intent intent = new Intent(MainActivity.this, Chitiet.class);
            intent.putExtra("id", truyenTranh.getId());  // Gửi ID truyện
            startActivity(intent);
        });
    }

    DbHelper dbHelper;

    private void loadData() {
        dbHelper = new DbHelper(this);
        truyenTranhArrayList.clear();  // Xóa danh sách cũ trước khi thêm dữ liệu mới
        truyenTranhArrayList.addAll(dbHelper.getAllTruyen());

        adapter.notifyDataSetChanged(); // Cập nhật adapter
    }






}
