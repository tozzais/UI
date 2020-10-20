package com.example.ui;


import android.os.Bundle;
import android.widget.TextView;

import com.gc.common.ui.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class CircleImageViewActivity extends BaseActivity {




    @Override
    public int getLayoutId() {
        return R.layout.activity_circle_image;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setBackTitle("圆形ImageView");
    }

    @Override
    public void loadData() {

    }



}