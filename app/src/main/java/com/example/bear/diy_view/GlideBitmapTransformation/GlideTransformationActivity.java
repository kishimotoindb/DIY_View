package com.example.bear.diy_view.GlideBitmapTransformation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.bear.diy_view.R;

public class GlideTransformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide_transformation);
        final ImageView imageView = (ImageView) findViewById(R.id.ivImageView);
        findViewById(R.id.btnLoad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(getBaseContext())
                        .load("http://img02.tooopen.com/images/20160509/tooopen_sy_161967094653.jpg")
                        .transform(new RoundBitmapTransformation(getBaseContext()))
                        .into(imageView);
            }
        });
    }
}
