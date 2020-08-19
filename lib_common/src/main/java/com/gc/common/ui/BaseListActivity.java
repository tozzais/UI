package com.gc.common.ui;

import android.os.Bundle;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gc.common.util.NetworkUtil;
import com.google.gson.JsonSyntaxException;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.gc.common.R;

import java.net.SocketTimeoutException;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import retrofit2.adapter.rxjava.HttpException;

public abstract class BaseListActivity<T> extends BaseActivity {

    protected int PageSize = 10;
    protected int DEFAULT_PAGE = 0;
    protected int page = DEFAULT_PAGE;
    protected BaseQuickAdapter mAdapter;



    public RecyclerView mRecyclerView;
    public SmartRefreshLayout swipeLayout;
    @Override
    public void initView(Bundle savedInstanceState) {
        mRecyclerView = findViewById(R.id.rv_list);
        swipeLayout = findViewById(R.id.swipeLayout);

    }

    @Override
    public void initListener() {
        //刷新
        if (swipeLayout != null)
        swipeLayout.setOnRefreshListener(refreshLayout -> onRefresh());
        //加载更多
        if (mAdapter.getLoadMoreModule() != null){
            mAdapter.getLoadMoreModule().setOnLoadMoreListener(() -> {
                loadData();
            });
            mAdapter.getLoadMoreModule().setAutoLoadMore(false);
            //当自动加载开启，同时数据不满一屏时，是否继续执行自动加载更多(默认为true)
            mAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
        }


    }


    protected void onRefresh() {
        // 这里的作用是防止下拉刷新的时候还可以上拉加载
        if (mAdapter.getLoadMoreModule() != null)
            mAdapter.getLoadMoreModule().setEnableLoadMore(false);
        page = DEFAULT_PAGE;
        loadData();
    }


    /**
     * 解决列表加载失败的
     *
     * @param e
     */
    public void onErrorResult(Throwable e) {
        String s = "";
        if (page != DEFAULT_PAGE) {
            page--;
            if (mAdapter.getLoadMoreModule() != null)
            mAdapter.getLoadMoreModule().loadMoreFail();
            return;
        } else if (!NetworkUtil.isNetworkAvailable(mActivity)) {
            s = getResources().getString(R.string.error_net);
        } else if (e != null && e instanceof SocketTimeoutException) {
            s = getResources().getString(R.string.error_timeout);
        } else if (e != null && e instanceof JsonSyntaxException) {
            s = getResources().getString(R.string.error_syntax);
        } else if (e != null && e instanceof HttpException) {
            s = getResources().getString(R.string.error_http);
        }else {
            s = e.getMessage();
        }
        if (!isLoad) {
            showError(s);
        } else {
            tsg(s);
        }

    }

    protected void setData(boolean isRefresh, List<T> data) {
        final int size = data == null ? 0 : data.size();
        if (isRefresh) {
            mAdapter.setNewData(data);
        } else {
            if (size > 0) {
                mAdapter.addData(data);
            }
        }
        if (size < PageSize) {
            if (mAdapter.getLoadMoreModule() != null)
            mAdapter.getLoadMoreModule().loadMoreEnd(isRefresh);
        } else {
            if (mAdapter.getLoadMoreModule() != null)
            mAdapter.getLoadMoreModule().loadMoreComplete();
        }
        isLoad = true;
    }

    protected void setData(List<T> data) {
        setData(page == DEFAULT_PAGE, data);
    }
}
