package com.example.bear.diy_view.LayoutProcedure;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.bear.diy_view.R;
/*
 *  点击requestLayout()
 *  03-02 12:18:17.199 22571-22571/com.example.bear.diy_view I/bear: onMeasure
    03-02 12:18:17.200 22571-22571/com.example.bear.diy_view I/bear: layout
    03-02 12:18:17.201 22571-22571/com.example.bear.diy_view I/bear: draw
    03-02 12:18:17.201 22571-22571/com.example.bear.diy_view I/bear: onDraw

    点击invalidate()
    03-02 12:18:21.113 22571-22571/com.example.bear.diy_view I/bear: draw
    03-02 12:18:21.113 22571-22571/com.example.bear.diy_view I/bear: onDraw
 */

public class RequestLayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_layout);
        final RequestLayoutTestView testView = findViewById(R.id.testView);
        View button1 = findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testView.setMyHeight(200);
                testView.requestLayout();
            }
        });

        View button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testView.invalidate();
            }
        });


    }
}
