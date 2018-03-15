package com.example.bear.diy_view._36ke;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.example.bear.diy_view.R;

public class PullToRefreshListActivity extends AppCompatActivity {

    private RefreshView mRefreshView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_to_refresh_list);

//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
//        View header = View.inflate(this, R.layout.header_36ke, null);

        mRefreshView = findViewById(R.id.refreshView);
        mRefreshView.setState(RefreshView.STATE_EXPANDING);
        mRefreshView.setExpandRatio(0);
    }

    float startY;
    float oldY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float rawY = event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = oldY = rawY;
                mRefreshView.setState(RefreshView.STATE_EXPANDING);
                mRefreshView.setExpandRatio(0);
                break;
            case MotionEvent.ACTION_MOVE:
                float newY = rawY;
                float ratio = Math.abs((newY - startY) / 500);
                if (ratio >= 1) {
                    mRefreshView.setState(RefreshView.STATE_FILLING);
                    ObjectAnimator anim = ObjectAnimator.ofFloat(mRefreshView, "FillCenterRatio", 0, 1);
                    anim.setDuration(2000).setInterpolator(new AccelerateDecelerateInterpolator());
                    anim.start();
                } else {
                    mRefreshView.setState(RefreshView.STATE_EXPANDING);
                    mRefreshView.setExpandRatio(ratio);
                }

                break;
        }

        return true;
    }
}
