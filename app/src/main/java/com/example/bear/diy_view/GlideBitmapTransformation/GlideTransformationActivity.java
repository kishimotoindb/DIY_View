package com.example.bear.diy_view.GlideBitmapTransformation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bear.diy_view.R;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class GlideTransformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide_transformation);
        final ImageView imageView = (ImageView) findViewById(R.id.ivImageView);

        Glide.with(getBaseContext())
                .load("http://img02.tooopen.com/images/20160509/tooopen_sy_161967094653.jpg")
                .apply(RequestOptions.bitmapTransform(new RoundBitmapTransformation()).autoClone())
//                .apply(RequestOptions.circleCropTransform())
                .into(imageView);

    }
}
