package com.example.btl_comicapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.btl_comicapp.object.TruyenTranh;
import com.example.btl_comicapp.database.DbHelper;

import java.util.ArrayList;

public class Chitiet extends AppCompatActivity {
    private TextView detailName, detailAuthor, detailDesc;
    private ImageView detailImg;
    private RecyclerView tagList;
    private String id;
    private TruyenTranh truyenTranh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitiet);

        initView(); // Ánh xạ view

        Intent intent = getIntent();
        id = intent.getStringExtra("id"); // Gán ID vào biến toàn cục

        if (id != null) {
            DbHelper db = new DbHelper(this);
            truyenTranh = db.getTruyenById(id);

            if (truyenTranh != null) {
                detailName.setText(truyenTranh.getTenTruyen());
                detailAuthor.setText(truyenTranh.getTacGia());
                detailDesc.setText(truyenTranh.getMoTa());
                Glide.with(this).load(truyenTranh.getLinkAnh()).into(detailImg);
            } else {
                detailName.setText("Không tìm thấy truyện!");
            }
        } else {
            detailName.setText("Lỗi ID truyện!");
        }
    }



    // Khởi tạo View
    private void initView() {
        detailName = findViewById(R.id.detail_name);
        detailAuthor = findViewById(R.id.detail_author);
        detailDesc = findViewById(R.id.detail_des);
        detailImg = findViewById(R.id.detail_ava);
        tagList = findViewById(R.id.tag_list);
    }

    // Mở danh sách chương

}
