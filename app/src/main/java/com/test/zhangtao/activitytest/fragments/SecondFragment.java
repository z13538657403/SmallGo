package com.test.zhangtao.activitytest.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.adapter.BaseAdapter;
import com.test.zhangtao.activitytest.adapter.HWAdapter;
import com.test.zhangtao.activitytest.bean.Page;
import com.test.zhangtao.activitytest.bean.Wares;
import com.test.zhangtao.activitytest.decoration.DividerItemDecoration1;
import com.test.zhangtao.activitytest.http.BaseCallBack;
import com.test.zhangtao.activitytest.http.DialogCallBack;
import com.test.zhangtao.activitytest.http.OkHttpHelper;
import com.test.zhangtao.activitytest.pagers.WareDetailActivity;
import com.test.zhangtao.activitytest.widget.Contants;
import java.io.IOException;
import java.util.List;

/**
 * Created by zhangtao on 16/9/25.
 */
public class SecondFragment extends BaseFragment
{
    private OkHttpHelper httpHelper = OkHttpHelper.getInstance();
    @ViewInject(R.id.refresh_view)
    private MaterialRefreshLayout refreshLayout;
    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;
    private HWAdapter myAdapter;
    private int currPage = 1;
    private int totalPage = 1;
    private int pageSize = 10;
    private List<Wares> datas;

    private static final int STATE_NORMAL= 0;
    private static final int STATE_REFRESH = 1;
    private static final int STATE_MORE = 2;
    private int state = STATE_NORMAL;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance)
    {
        return inflater.inflate(R.layout.second_fragment , container , false);
    }

    @Override
    public void init()
    {
        initRefreshLayout();
        getData();
    }

    private void initRefreshLayout()
    {
        refreshLayout.setLoadMore(true);
        refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener()
        {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout)
            {
                refreshData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout)
            {
                if (currPage <= totalPage)
                {
                    loadMoreData();
                }
                else
                {
                    Toast.makeText(getContext() , "没有下一页数据了" , Toast.LENGTH_SHORT).show();
                    refreshLayout.finishRefreshLoadMore();
                }
            }
        });
    }

    private void refreshData()
    {
        currPage = 1;
        state = STATE_REFRESH;
        getData();
    }

    private void loadMoreData()
    {
        currPage = ++currPage;
        state = STATE_MORE;
        getData();
    }

    private void getData()
    {
        String url = Contants.API.WARES_BASE + "?curPage=" + currPage + "&pageSize=" + pageSize;

        httpHelper.get(url , new DialogCallBack<Page<Wares>>(getActivity()) {

            @Override
            public void onSuccess(Response response, Page<Wares> waresPage)
            {
                datas = waresPage.getList();
                currPage = waresPage.getCurrentPage();
                totalPage = waresPage.getTotalPage();
                showToList();
            }

            @Override
            public void onError(Response response, int code, Exception e)
            {
            }
        });
    }

    private void showToList()
    {
        switch (state)
        {
            case STATE_NORMAL:
                myAdapter = new HWAdapter(getContext() , datas);
                myAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener()
                {
                    @Override
                    public void onClick(View view, int position)
                    {
                        Wares wares = myAdapter.getItem(position);
                        Intent intent = new Intent(getActivity() , WareDetailActivity.class);
                        intent.putExtra(Contants.DETAIL_WARES , wares);
                        startActivity(intent);
                    }
                });

                mRecyclerView.setAdapter(myAdapter);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mRecyclerView.addItemDecoration(new DividerItemDecoration1(getContext() , DividerItemDecoration1.VERTICAL_LIST));
                break;
            case STATE_REFRESH:
                myAdapter.clearData();
                myAdapter.addData(datas);

                mRecyclerView.scrollToPosition(0);
                refreshLayout.finishRefresh();
                break;
            case STATE_MORE:
                myAdapter.addData(myAdapter.getData().size() , datas);
                mRecyclerView.scrollToPosition(myAdapter.getData().size());
                refreshLayout.finishRefreshLoadMore();
                break;
        }
    }
}
