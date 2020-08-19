package com.example.ui;


import android.os.Bundle;
import android.widget.TextView;

import com.gc.ui.ui.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {


    @BindView(R.id.tv_name)
    TextView tvName;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setBackTitle("首页");
    }

    @Override
    public void loadData() {
        tvName.setText("首页");

    }


    @OnClick(R.id.tv_name)
    public void onViewClicked() {
        tvName.setText("首页111");
    }
}