package com.tozzais.baselibrary.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.tozzais.baselibrary.R;
import com.tozzais.baselibrary.util.StatusBarUtil;
import com.tozzais.baselibrary.util.toast.ToastCommom;
import com.tozzais.baselibrary.weight.ProgressLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.ButterKnife;


public abstract class BaseActivity<T> extends AppCompatActivity {

    private Toolbar mToolbar;
    public TextView mTitle;
    protected ProgressLayout progress_layout;
    protected TextView tv_right;
    protected ImageView iv_right_icon;
    protected View line;

    protected boolean isLoad = false;


    protected Context mContext;
    protected Activity mActivity;




    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getBaseLayout());

        line = findViewById(R.id.line);
        //设置只能屏幕方向
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mContext = this;
        mActivity = this;

        progress_layout = findViewById(R.id.progress_layout);
        AppBarLayout mHeaderView = findViewById(R.id.layout_header);
        FrameLayout mFlContainer = findViewById(R.id.content_container);


        int toolbarLayoutRes = getToolbarLayout();
        if (toolbarLayoutRes >= 0 && mHeaderView != null) {
            //添加toolbar
            mHeaderView.setVisibility(View.VISIBLE);
            mHeaderView.addView(LayoutInflater.from(mContext).inflate(toolbarLayoutRes, mHeaderView, false));
        } else if (mHeaderView != null) {
            mHeaderView.setVisibility(View.GONE);
        }
        //内容View
        if (mFlContainer != null && getLayoutId() != -1)
            mFlContainer.addView(LayoutInflater.from(mContext).inflate(getLayoutId(), mHeaderView, false));

        EventBus.getDefault().register(this);
        ButterKnife.bind(this);


        initView(savedInstanceState);

        loadData();
        initListener();


    }

    protected void setLineVisibility(){
        line.setVisibility(View.VISIBLE);
    }



    public void back(){
        finish();
    }
    @Override
    public void onBackPressed() {
//        LogUtil.e("onBackPressed");
        back();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setStatusBar();
    }


    protected void setStatusBar() {
        int mColor = getResources().getColor(R.color.white);
        StatusBarUtil.setColor(this, mColor, 0);
        StatusBarUtil.setLightMode(this);
    }

    protected void setStatusBar(int tag) {
        int mColor = getResources().getColor(R.color.white);
        StatusBarUtil.setColor(this, mColor, 0);
        StatusBarUtil.setLightMode(this);
    }

    protected int getBaseLayout() {
        return R.layout.base_activity;
    }

    protected int getToolbarLayout() {
        return R.layout.base_toolbar;
    }

    /**
     * 初始化 标题栏 只能内部调用
     */
    private void initToolbar(String title, boolean isCanBack) {
        mToolbar = findViewById(R.id.toolbar);
        if (mToolbar != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mToolbar.setElevation(0);
            }
            mToolbar.setTitle("");
            setSupportActionBar(mToolbar);
            mTitle = findViewById(R.id.tv_title);
            tv_right = findViewById(R.id.tv_right);
            iv_right_icon = findViewById(R.id.iv_right_icon);
            mTitle.setText(title);

            if (isCanBack) {
                mToolbar.setNavigationIcon(R.mipmap.back);
                mToolbar.setNavigationOnClickListener(view -> back());
            } else {
                mToolbar.setNavigationIcon(null);
            }
        }
    }



    public abstract int getLayoutId();

    public abstract void initView(Bundle savedInstanceState);

    public abstract void loadData();


    public  void initListener() {};

    public void setBackTitle(String title) {
        initToolbar(title, true);
    }

    public void setBackTitle(int title) {
        initToolbar(getResources().getString(title), true);
    }

    public void setNotBackTitle(String title) {
        initToolbar(title, false);
    }

    public void setRightIcon(int res) {
        if (iv_right_icon != null) {
            iv_right_icon.setVisibility(View.VISIBLE);
            iv_right_icon.setImageResource(res);
        }
    }

    public void setRightText(String res) {
        if (tv_right != null) {
            tv_right.setVisibility(View.VISIBLE);
            tv_right.setText(res);
            tv_right.setOnClickListener(v -> rightTextClick());
        }
    }

    public  void rightTextClick(){}

    public void setNotBackTitle(int title) {
        initToolbar(getResources().getString(title), false);
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(T t) {

    }

    protected void tsg(String str) {  //封装弹出框提示

        ToastCommom.createToastConfig().ToastShow(mContext, str);
    }

    protected void showProress() {
        if (progress_layout != null)
        progress_layout.showLoading();
    }

    protected void showContent() {
        isLoad = true;
        if (progress_layout != null)
        progress_layout.showContent();
    }

    protected void showError() {
        if (progress_layout != null)
        progress_layout.showError(R.string.loading_error, view -> loadData());
    }

    protected void showError(String message) {
        if (progress_layout != null)
        progress_layout.showError(message, view -> loadData());
    }



}
