package com.tozzais.baselibrary.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.JsonSyntaxException;
import com.tozzais.baselibrary.R;
import com.tozzais.baselibrary.util.NetworkUtil;

import java.net.SocketTimeoutException;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.adapter.rxjava.HttpException;


/**
 * Created by Administrator on 2017/4/19.
 */

public abstract class BaseListFragment<T> extends BaseFragment {


    protected BaseQuickAdapter mAdapter;
    protected int DEFAULT_PAGE = 1;
    protected int page = DEFAULT_PAGE;
    protected int PageSize = 10;

    public RecyclerView mRecyclerView;
    public SwipeRefreshLayout swipeLayout;
    @Override
    public void initView(Bundle savedInstanceState) {
        mRecyclerView = mRootView.findViewById(R.id.rv_list);
        swipeLayout = mRootView.findViewById(R.id.swipeLayout);
    }

    @Override
    public int setLayout() {
        return R.layout.base_fragment_recycleview;
    }


    @Override
    public void loadData() {
        if (!isLoad) {
            showProress();
        }
    }

    @Override
    public void initListener() {
        //刷新
        if (swipeLayout != null)
        swipeLayout.setOnRefreshListener(this::onRefresh);
        //加载更多
        if (mAdapter.getLoadMoreModule() != null){
            mAdapter.getLoadMoreModule().setOnLoadMoreListener(() -> {
                loadData();
                loadData1();
            });
            mAdapter.getLoadMoreModule().setAutoLoadMore(true);
            //当自动加载开启，同时数据不满一屏时，是否继续执行自动加载更多(默认为true)
            mAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
        }


    }





    protected void setEmptyView(int res, String tip, String btnStrng, View.OnClickListener listener) {
        View empty_view = View.inflate(mActivity, R.layout.base_empty_view, null);
        TextView tv_content = empty_view.findViewById(R.id.tv_content);
        ImageView iv_icon = empty_view.findViewById(R.id.iv_icon);
        TextView tv_btn = empty_view.findViewById(R.id.tv_btn);
        tv_btn.setVisibility(TextUtils.isEmpty(btnStrng) ? View.GONE : View.VISIBLE);
        tv_btn.setText(btnStrng);
        iv_icon.setImageResource(res);
        if (listener != null)
            tv_btn.setOnClickListener(listener);
        tv_content.setText(tip);
        mAdapter.setEmptyView(empty_view);
    }

    protected void setEmptyView(String tip) {
        setEmptyView(R.mipmap.empty_view, tip, null, null);
    }


    protected void setEmptyView(int res, String tip) {
        setEmptyView(res, tip, null, null);
    }


    protected void onRefresh() {
        swipeLayout.setRefreshing(true);
        // 这里的作用是防止下拉刷新的时候还可以上拉加载
        if ( mAdapter.getLoadMoreModule() != null)
        mAdapter.getLoadMoreModule().setEnableLoadMore(false);
        page = DEFAULT_PAGE;
        loadData();
    }


    protected void setData(boolean isRefresh, List<T> data) {
        showContent();
        int size = data == null ? 0 : data.size();
        if (isRefresh) {
            mAdapter.setNewData(data);
        } else {
            if (size > 0) {
                mAdapter.addData(data);
            }
        }
        if (size < PageSize) {
            /**
             *  参数是 false的话 显示 没有更多数据
             *  参数是 true的话 不显示
             */
            if (mAdapter.getLoadMoreModule() != null)
            mAdapter.getLoadMoreModule().loadMoreEnd(isRefresh);
        } else {
            //自动加载下一个 显示加载中
            if (mAdapter.getLoadMoreModule() != null)
            mAdapter.getLoadMoreModule().loadMoreComplete();
        }
        isLoad = true;
        page++;
    }

    protected void setData(List<T> data) {
        setData(page == DEFAULT_PAGE, data);
    }


    /**
     * 解决列表加载失败的
     *
     * @param e
     */
    public void onErrorResult(Throwable e) {
        if (swipeLayout != null)
            swipeLayout.setRefreshing(false);
        String s = "";
        if (page != DEFAULT_PAGE) {
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
        } else {
            s = e.getMessage();
        }
        if (!isLoad) {
            showError(s);
        } else {
            tsg(s);
        }

    }

    //返回数据 code不等于500的
    public void onErrorResult(String s) {
        if (swipeLayout != null)
            swipeLayout.setRefreshing(false);
        if (page != DEFAULT_PAGE) {
            if (mAdapter.getLoadMoreModule() != null)
            mAdapter.getLoadMoreModule().loadMoreFail();
            return;
        }
        if (!isLoad) {
            showError(s);
        } else {
            tsg(s);
        }

    }

    @Override
    protected void showError(String s) {
        super.showError(s);
        isLoad = false;
    }

    @Override
    protected void showContent() {
        isLoad = true;
        super.showContent();
        if (swipeLayout != null)
            swipeLayout.setRefreshing(false);

    }

}
