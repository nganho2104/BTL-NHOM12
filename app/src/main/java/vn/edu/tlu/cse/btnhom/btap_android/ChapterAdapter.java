package vn.edu.tlu.cse.btnhom.btap_android;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.edu.tlu.cse.btnhom.btap_android.readcomic.ReadChapterActivity;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterVH> {
    private Context context;
    private List<String> chapters;  // chapter index (1,2,3,...)
    private List<String> links;     // Firebase links (optional, can be ignored)
    private String storyId;         // ID of the story (e.g. "2")

    public ChapterAdapter(Context context, List<String> chapters, List<String> links, String storyId) {
        this.context = context;
        this.chapters = chapters;
        this.links = links;
        this.storyId = storyId;
    }

    @NonNull
    @Override
    public ChapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ChapterVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterVH holder, int position) {
        String chapterNum = chapters.get(position);
        holder.tv.setText("Chương " + chapterNum);

        holder.itemView.setOnClickListener(v -> {
            Intent i = new Intent(context, ReadChapterActivity.class);
            i.putExtra("storyId", Integer.parseInt(storyId));            // truyền ID truyện
            i.putExtra("chapterIndex", Integer.parseInt(chapterNum));    // truyền số chương
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return chapters.size();
    }

    static class ChapterVH extends RecyclerView.ViewHolder {
        TextView tv;

        public ChapterVH(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(android.R.id.text1);
        }
    }
}
