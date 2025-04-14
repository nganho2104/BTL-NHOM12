package vn.edu.tlu.cse.btnhom.btap_android;

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
    private boolean isFavoriteScreen;
    private String role;

    public StoryAdapter(Context context, List<Story> list, boolean isFavoriteScreen, String role) {
        this.context = context;
        this.list = list;
        this.isFavoriteScreen = isFavoriteScreen;
        this.role = role;
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

        // Logic hiển thị nút
        if (isFavoriteScreen) {
            // Trong TỦ TRUYỆN: chỉ hiện nút xóa
            holder.btnFavorite.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.VISIBLE);

            holder.btnDelete.setOnClickListener(v -> {
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference favRef = FirebaseDatabase.getInstance().getReference("favorites")
                        .child(userId).child(s.getId());
                favRef.removeValue().addOnSuccessListener(unused -> {
                    Toast.makeText(context, "Đã xoá khỏi tủ truyện", Toast.LENGTH_SHORT).show();
                    int pos = holder.getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        list.remove(pos);
                        notifyItemRemoved(pos);
                        notifyItemRangeChanged(pos, list.size());
                    }
                });
            });

        } else {
            // Màn hình chính

            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                // Không đăng nhập: ẩn cả hai nút
                holder.btnFavorite.setVisibility(View.GONE);
                holder.btnDelete.setVisibility(View.GONE);
            } else if ("tacgia".equals(role)) {
                // Đăng nhập với role là tacgia: hiện cả hai nút
                holder.btnFavorite.setVisibility(View.VISIBLE);
                holder.btnDelete.setVisibility(View.VISIBLE);
            } else {
                // Đăng nhập với role khác: chỉ hiện favorite
                holder.btnFavorite.setVisibility(View.VISIBLE);
                holder.btnDelete.setVisibility(View.GONE);
            }

            holder.btnFavorite.setOnClickListener(v -> {
                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    Toast.makeText(context, "Vui lòng đăng nhập để lưu truyện", Toast.LENGTH_SHORT).show();
                    return;
                }
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference favRef = FirebaseDatabase.getInstance().getReference("favorites")
                        .child(userId).child(s.getId());
                favRef.setValue(true).addOnSuccessListener(unused ->
                        Toast.makeText(context, "Đã lưu vào tủ truyện", Toast.LENGTH_SHORT).show());
            });

            holder.btnDelete.setOnClickListener(v -> {
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference storyRef = FirebaseDatabase.getInstance().getReference("stories").child(s.getId());
                storyRef.removeValue().addOnSuccessListener(unused ->
                        Toast.makeText(context, "Đã xoá truyện", Toast.LENGTH_SHORT).show());
            });
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    static class StoryVH extends RecyclerView.ViewHolder {
        ImageView img, btnFavorite, btnDelete;
        TextView tvTitle, tvAuthor, tvChap;
        public StoryVH(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgStory);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvChap = itemView.findViewById(R.id.tvChap);
            btnFavorite = itemView.findViewById(R.id.btnFavorite);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}