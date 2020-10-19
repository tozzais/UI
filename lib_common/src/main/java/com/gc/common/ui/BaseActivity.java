package com.gc.common.ui;

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

import com.gc.common.util.StatusBarUtil;
import com.gc.common.util.toast.ToastCommom;
import com.gc.common.weight.ProgressLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.gc.common.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 *  Created by xmm on 2020/08/21.
 *  成员变量
 *  重写的方法
 *  自定义的方法
 * @param <T>
 */
public abstract class BaseActivity<T> extends AppCompatActivity {

    protected Context mContext;
    protected Activity mActivity;
    /**
     *  下面的控件都是最底层布局中控件。如果重写 getBaseLayout() 方法 这些控件则为空
     */
    private ProgressLayout progress_layout;
    private View line;
    private Toolbar mToolbar;
    private TextView mTitle;
    private TextView tv_right;
    private ImageView iv_right_icon;
    /**
     * 页面是否加载
     */
    protected boolean isLoad = false;

    private Unbinder butterKnifeBind;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getBaseLayout());
        //设置只能屏幕方向
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mContext = this;
        mActivity = this;
        //初始化 最底层view
        initBaseView();
        //初始化 标题栏view
        initToolBarView();
        //初始化 内容view
        initContentView();
        //第三方的舒适化
        EventBus.getDefault().register(this);
        butterKnifeBind = ButterKnife.bind(this);
        //提取抽象 方法
        initView(savedInstanceState);
        loadData();
        initListener();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setStatusBar();
    }

    @Override
    public void onBackPressed() {
        back();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (butterKnifeBind != null)
            butterKnifeBind.unbind();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(T t) {

    }

    protected int getBaseLayout() {
        return R.layout.base_activity;
    }

    protected int getToolbarLayout() {
        return R.layout.base_toolbar;
    }


    public abstract int getLayoutId();

    public abstract void initView(Bundle savedInstanceState);

    public abstract void loadData();

    public  void initListener() {}

    /**
     * 初始化 最底层view
     */
    private  void  initBaseView(){
        progress_layout = findViewById(R.id.progress_layout);
        line = findViewById(R.id.line);
    }

    /**
     * 初始化 标题栏view
     */
    private  void  initToolBarView(){
        AppBarLayout mHeaderView = findViewById(R.id.layout_header);
        int toolbarLayoutRes = getToolbarLayout();
        if (toolbarLayoutRes >= 0 && mHeaderView != null) {
            //添加toolbar
            mHeaderView.setVisibility(View.VISIBLE);
            mHeaderView.addView(LayoutInflater.from(mContext).inflate(toolbarLayoutRes, mHeaderView, false));
        } else if (mHeaderView != null) {
            mHeaderView.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化 内容view
     */
    private void initContentView(){
        FrameLayout mFlContainer = findViewById(R.id.content_container);
        int layoutId = getLayoutId();
        if (mFlContainer != null && layoutId != -1)
            mFlContainer.addView(LayoutInflater.from(mContext).inflate(layoutId, mFlContainer, false));

    }

    /**
     * 设置标题 栏到内容的线是否可见
     */
    protected void setLineVisibility(int visibility){
        line.setVisibility(visibility);
    }

    /**
     * 重写返回按钮
     */
    public void back(){
        finish();
    }

    /**
     * 设置状态栏样式
     */
    protected void setStatusBar() {
        int mColor = getResources().getColor(R.color.white);
        StatusBarUtil.setColor(this, mColor, 0);
        StatusBarUtil.setLightMode(this);
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



    protected void tsg(String str) {
        ToastCommom.createToastConfig().ToastShow(mContext, str);
    }

    protected void showProgress() {
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
