package vn.edu.tlu.cse.btnhom.btap_android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import vn.edu.tlu.cse.btnhom.btap_android.Story;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoryVH> {

    private Context context;
    private List<Story> list;

    public StoryAdapter(Context context, List<Story> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public StoryVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_story, parent, false);
        return new StoryVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryVH holder, int position) {
        Story s = list.get(position);
        holder.tvTitle.setText(s.getTenTruyen());
        holder.tvAuthor.setText("Tác giả: " + s.getTacGia());
        holder.tvChap.setText("Chap: " + s.getTenChap());
        Glide.with(context).load(s.getLinkAnh()).placeholder(R.drawable.ic_launcher_background).into(holder.img);

        holder.itemView.setOnClickListener(v -> {
            Intent i = new Intent(context, StoryDetailActivity.class);
            i.putExtra("story", s);
            context.startActivity(i);
        });

        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xoá truyện")
                    .setMessage("Bạn có chắc chắn muốn xoá?")
                    .setPositiveButton("Xoá", (dialog, which) -> {
                        FirebaseDatabase.getInstance().getReference("stories")
                                .child(s.getId())
                                .removeValue()
                                .addOnSuccessListener(unused -> Toast.makeText(context, "Đã xoá", Toast.LENGTH_SHORT).show());
                    })
                    .setNegativeButton("Huỷ", null)
                    .show();
            return true;
        });

        holder.btnFavorite.setOnClickListener(v -> {
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                Toast.makeText(context, "Vui lòng đăng nhập để lưu truyện", Toast.LENGTH_SHORT).show();
                return;
            }

            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference favRef = FirebaseDatabase.getInstance().getReference("favorites")
                    .child(userId).child(s.getId());

            favRef.setValue(true)
                    .addOnSuccessListener(unused ->
                            Toast.makeText(context, "Đã lưu vào tủ truyện", Toast.LENGTH_SHORT).show());
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class StoryVH extends RecyclerView.ViewHolder {
        ImageView img, btnFavorite;
        TextView tvTitle, tvAuthor, tvChap;
        public StoryVH(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgStory);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvChap = itemView.findViewById(R.id.tvChap);
            btnFavorite = itemView.findViewById(R.id.btnFavorite);
        }
    }
}
