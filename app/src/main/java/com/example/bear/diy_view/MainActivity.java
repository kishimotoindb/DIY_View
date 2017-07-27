package com.example.bear.diy_view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private LabelWall mWall;
    private LinearLayout mLl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWall = (LabelWall) findViewById(R.id.wall);
    }

    int i = 0;

    public void onClick(View view) {
        View inflate = View.inflate(this, R.layout.item_label, null);
        TextView tv = (TextView) inflate.findViewById(R.id.tv);
        String text = tv.getText().toString();
        tv.setText(text + i++);
        mWall.addView(inflate);
    }
}
