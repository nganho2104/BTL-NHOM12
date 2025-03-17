package com.example.btl_comicapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.btl_comicapp.Chitiet;
import com.example.btl_comicapp.R;
import com.example.btl_comicapp.object.TruyenTranh;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.Collections;
import java.util.Comparator;
public class TruyenTranhAdapter extends ArrayAdapter<TruyenTranh> {
    private Context ct;
    private List<TruyenTranh> originalList; // Danh sách gốc
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(TruyenTranh truyenTranh);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public TruyenTranhAdapter(@NonNull Context context, int resource, @NonNull List<TruyenTranh> objects) {
        super(context, resource, objects);
        this.ct = context;
        this.originalList = new ArrayList<>(objects); // Lưu lại danh sách gốc

    }

    public void sortTruyen(String s) {
        List<TruyenTranh> filteredList = new ArrayList<>();
        if (s.isEmpty()) {
            // Nếu không nhập gì, hiển thị lại toàn bộ
            filteredList.addAll(originalList);
        } else {
            for (TruyenTranh truyen : originalList) {
                if (truyen.getTenTruyen().toLowerCase().contains(s.toLowerCase())) {
                    filteredList.add(truyen);
                }
            }
        }
        clear();
        addAll(filteredList);
        notifyDataSetChanged();
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) ct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.iteam_truyen, null);
        }

        TruyenTranh truyenTranh = getItem(position);
        if (truyenTranh != null) {
            TextView tenChap = convertView.findViewById(R.id.txvTenChap);
            TextView tenTruyen = convertView.findViewById(R.id.txvTenTruyen);
            ImageView imgAnhTruyen = convertView.findViewById(R.id.imgAnhTruyen);

            tenTruyen.setText(truyenTranh.getTenTruyen());
            tenChap.setText(truyenTranh.getTenChap());

            // Tải ảnh qua Glide (cần INTERNET permission)
            Glide.with(ct).load(truyenTranh.getLinkAnh()).into(imgAnhTruyen);
        }
        return convertView;
    }
}

