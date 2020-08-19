package com.gc.ui.ui;


import android.os.Bundle;

public abstract class LazyListFragment<T> extends BaseListFragment<T> {



    // 控件是否初始化完成
    protected boolean isViewInitiated;

    // 页面是否可见
    protected boolean isVisibleToUser;

    // 每一次当前Fragment可见时，会调用该方法，并且isVisibleToUser字段会置为true，
    // 我们可以通过此字段判断当前Fragment是否可见；
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        // 当前可见时，我们尝试去加载数据
        prepareFetchData(false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        prepareFetchData(false);
    }

    @Override
    protected void onRefresh() {
        super.onRefresh();
        loadData1();
    }

    @Override
    public void loadData() {
        super.loadData();
    }


    @Override
    public void initListener() {
        super.initListener();

    }


    protected void prepareFetchData(boolean forceUpdate) {
        // 由于setUserVisibleHint方法在onActivityCreated等方法之前调用，所以加载数据时机是不太合适的
        // 所以最好是当前Fragment可见，并且当前Fragment中相关View控件都初始化ok时我们在调用数据请求刷新
        // 通过isViewInitiated为true来判断View控件是否初始化成功
        if (isVisibleToUser && isViewInitiated && (!isLoad || forceUpdate)) {
            loadData1();
        }
    }


}
