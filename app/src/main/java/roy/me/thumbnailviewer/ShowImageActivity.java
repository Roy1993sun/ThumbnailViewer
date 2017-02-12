package roy.me.thumbnailviewer;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ShowImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        ImageView ivShowImage = (ImageView) findViewById(R.id.iv_show_image);
        Intent latestActivity = getIntent();
        if (latestActivity != null) {
            Uri imageUri = latestActivity.getData();
            if (imageUri != null && ivShowImage!= null)
            Glide.with(this).load(imageUri).into(ivShowImage);

        }

    }
}
