package com.example.bear.diy_view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.bear.diy_view.GlideBitmapTransformation.GlideTransformationActivity;
import com.example.bear.diy_view._36ke.PullToRefreshListActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    final String KEY_1 = "KEY_1";
    final String KEY_2 = "KEY_2";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = (ListView) findViewById(R.id.listView);

        ArrayList<Map<String, Object>> data = new ArrayList<>();

        //第一个示例：GlideTransformation
        HashMap<String, Object> item1 = new HashMap<>();
        item1.put(KEY_1, "GlideTransformation");
        item1.put(KEY_2, GlideTransformationActivity.class);
        data.add(item1);

        //第二个示例：仿36氪下拉加载更多动画
        HashMap<String, Object> item2 = new HashMap<>();
        item2.put(KEY_1, "仿36氪下拉加载更多动画");
        item2.put(KEY_2, PullToRefreshListActivity.class);
        data.add(item2);

        listView.setAdapter(new SimpleAdapter(
                this,
                data,
                android.R.layout.simple_list_item_1,
                new String[]{KEY_1},
                new int[]{android.R.id.text1})
        );


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> item = (Map<String, Object>) parent.getItemAtPosition(position);
                startActivity(new Intent(MainActivity.this, (Class<?>) item.get(KEY_2)));
            }
        });
    }
}
