package vn.edu.tlu.cse.btnhom.btap_android;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import vn.edu.tlu.cse.btnhom.btap_android.R;
import vn.edu.tlu.cse.btnhom.btap_android.Story;

public class StoryDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_story_detail);
        Story s = (Story) getIntent().getSerializableExtra("story");
        if (s == null) finish();

        ImageView img = findViewById(R.id.imgDetail);
        TextView tvTitle = findViewById(R.id.tvDetailTitle);
        TextView tvAuthor = findViewById(R.id.tvDetailAuthor);
        TextView tvDesc = findViewById(R.id.tvDetailDesc);

        tvTitle.setText(s.getTenTruyen());
        tvAuthor.setText("Tác giả: " + s.getTacGia());
        tvDesc.setText(s.getMoTa());
        Glide.with(this).load(s.getLinkAnh()).into(img);
    }
}