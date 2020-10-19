package com.gc.common.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.gc.common.util.toast.ToastCommom;
import com.gc.common.weight.ProgressLayout;
import com.gc.common.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by jumpbox on 16/4/7.
 */
public abstract class BaseFragment<T> extends DialogFragment {

    protected Activity mActivity;
    /**
     * 下面的控件都是最底层布局中控件。如果重写 getBaseLayout() 方法 这些控件则为空
     */
    public View mRootView;
    protected RelativeLayout mHeaderView;
    protected FrameLayout mContainerView;
    protected ProgressLayout progress_layout;

    protected boolean isInit = false; //是否初始化
    protected boolean isLoad = false; //是否加载完成
    private Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(getBaseLayout(), container, false);

            int titleLayoutRes = getTitleLayout();
            if (titleLayoutRes > 0) {
                //添加头布局
                mHeaderView =  mRootView.findViewById(R.id.rl_header);
                mHeaderView.addView(LayoutInflater.from(mActivity).inflate(titleLayoutRes, mHeaderView, false));
            }

            //添加内容区域
            mContainerView =  mRootView.findViewById(R.id.content_container);
            mContainerView.addView(LayoutInflater.from(mActivity).inflate(setLayout(), mContainerView, false));

            //加载
            progress_layout =  mRootView.findViewById(R.id.progress_layout);

            EventBus.getDefault().register(this);
            unbinder = ButterKnife.bind(this, mRootView);

            initView(savedInstanceState);
            initView1(savedInstanceState);
        }


        return mRootView;
    }
    @Subscribe
    public void onEvent(T t) {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();
        initListener();
    }

    protected int getTitleLayout() {
        return -1;
    }
    protected int getBaseLayout() {
        return R.layout.base_fragment;
    }

    public void initTitle() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null){
            unbinder.unbind();
        }
    }

    protected void tsg(String str){
        ToastCommom.createToastConfig().ToastShow(getContext(),str);
    }


    public void initEventBus(){
        boolean registered = EventBus.getDefault().isRegistered(this);
        if (!registered){
            EventBus.getDefault().register(this);
        }
    }

    public abstract int setLayout();

    public  void initView(Bundle savedInstanceState){

    }


    public abstract void loadData();


    //懒加载用的
    public  void loadData1(){}


    //主要是为了ListFragmentview的初始化。放置viewpager的预加载导致 RecyclerView和SwipeRefreshLayout的空指针
    public void initView1(Bundle savedInstanceState){};

    public  void initListener(){}

    public  void scrollTop(){}

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    protected void showProress() {
        progress_layout.showLoading();
    }

    protected void showContent() {
        progress_layout.showContent();
    }

    protected void showError() {
        showError(getString(R.string.loading_error));
    }

    protected void showError(int errorStr) {
        showError(getString(errorStr));
    }

    protected void showError(String errorStr) {
        progress_layout.showError(errorStr, v -> {
            loadData();
            loadData1();
        });
    }

    protected void showError(String errorStr, View.OnClickListener clickListener) {
        progress_layout.showError(errorStr, clickListener);
    }

    protected void showError(String errorStr, String btnStr, View.OnClickListener clickListener) {
        progress_layout.showError(errorStr, btnStr,clickListener);
    }
    protected void hideHeader() {
        if (null != mHeaderView) {
            mHeaderView.setVisibility(View.GONE);
        }
    }
}
