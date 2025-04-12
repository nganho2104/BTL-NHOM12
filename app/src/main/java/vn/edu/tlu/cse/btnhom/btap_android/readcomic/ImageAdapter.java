package vn.edu.tlu.cse.btnhom.btap_android.readcomic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import vn.edu.tlu.cse.btnhom.btap_android.R;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageVH> {
    private Context context;
    private List<String> images;

    public ImageAdapter(Context context, List<String> images) {
        this.context = context;
        this.images = images;
    }

    @NonNull
    @Override
    public ImageVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
        return new ImageVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageVH holder, int position) {
        Glide.with(context).load(images.get(position)).into(holder.imgPage);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    static class ImageVH extends RecyclerView.ViewHolder {
        ImageView imgPage;
        public ImageVH(@NonNull View itemView) {
            super(itemView);
            imgPage = itemView.findViewById(R.id.imgPage);
        }
    }
}
