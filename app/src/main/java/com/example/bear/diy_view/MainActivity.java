package com.example.bear.diy_view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.R.attr.label;

public class MainActivity extends AppCompatActivity {

    private LabelWall mWall;
    private ArrayList<String> mLabels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWall = (LabelWall) findViewById(R.id.wall);
        mLabels = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            mLabels.add("小袋鼠嘤嘤嘤" + i);
        }
    }

    public void onClick(View view) {
        mWall.setAdapter(new MyAdatper(mLabels));
        mWall.addOnLabelClickListener(new LabelWall.OnLabelClickListener() {
            @Override
            public void onLabelClick(LabelWall labelWall, View v, int position, int id) {
                labelWall.getAdapter().getData().remove(position);
                labelWall.getAdapter().notifyDataSetChanged();
            }
        });
    }

    static class MyAdatper extends LabelWall.LabelAdapter<String> {
        public MyAdatper(ArrayList<String> data) {
            super(data);
        }

        @Override
        public View getView(LabelWall parent, View convertView, int position) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(parent.getContext(), R.layout.item_label, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv.setText(mData.get(position));
            return convertView;
        }

        static class ViewHolder {
            TextView tv;

            public ViewHolder(View view) {
                tv = (TextView) view.findViewById(R.id.tv);
            }
        }

    }

}
