package com.test.zhangtao.activitytest.pagers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.adapter.HWAdapter;
import com.test.zhangtao.activitytest.bean.Page;
import com.test.zhangtao.activitytest.bean.Wares;
import com.test.zhangtao.activitytest.decoration.DividerItemDecoration1;
import com.test.zhangtao.activitytest.http.BaseCallBack;
import com.test.zhangtao.activitytest.http.DialogCallBack;
import com.test.zhangtao.activitytest.http.OkHttpHelper;
import com.test.zhangtao.activitytest.widget.CnToolBar;
import com.test.zhangtao.activitytest.widget.Contants;
import com.test.zhangtao.activitytest.widget.WrapContentGridLayoutManager;
import com.test.zhangtao.activitytest.widget.WrapContentLinearLayoutManager;

import java.io.IOException;
import java.util.List;

/**
 * Created by zhangtao on 16/10/31.
 */
public class WareListActivity extends BaseActivity
{
    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    private List<Wares> datas;
    private Page<Wares> pageWares;
    private static final String TAG = "WareListActivity";
    private static final int STATE_NORMAL= 0;
    private static final int STATE_REFRESH = 1;
    private static final int STATE_MORE = 2;
    private int state = STATE_NORMAL;

    private static final int ACTION_LIST = 1;
    private static final int ACTION_GRID = 2;


    @ViewInject(R.id.ware_toolBar)
    private CnToolBar mToolBar;
    @ViewInject(R.id.tab_layout)
    private TabLayout mTabLayout;
    @ViewInject(R.id.txt_summary)
    private TextView mTxtSummary;
    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.refresh_layout)
    private MaterialRefreshLayout mRefreshLayout;

    private HWAdapter mWareApdater;

    private int currentPage = 1;
    private int totalPage = 1;
    private int pageSize = 10;
    private long campaignId;
    private int orderBy = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_warelist);
        ViewUtils.inject(WareListActivity.this);
        campaignId =  getIntent().getLongExtra(Contants.CAMPAIGN_ID , 0);

        initToolBar();
        initTab();
        initRefreshLayout();
        getData();
    }

    private void initToolBar()
    {
        mToolBar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                WareListActivity.this.finish();
            }
        });

        mToolBar.setRightButtonIcon(R.drawable.icon_grid_32);
        mToolBar.getRightButton().setTag(ACTION_LIST);
        mToolBar.setRightButtonOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int action = (int) view.getTag();
                if (ACTION_LIST == action)
                {
                    mToolBar.setRightButtonIcon(R.drawable.icon_list_32);
                    mToolBar.getRightButton().setTag(ACTION_GRID);
                    mWareApdater.resetLayout(R.layout.template_grid_wares);
                    mRecyclerView.setLayoutManager(new WrapContentGridLayoutManager(WareListActivity.this , 2));
                    mRecyclerView.setAdapter(mWareApdater);
                }
                else if (ACTION_GRID == action)
                {
                    mToolBar.setRightButtonIcon(R.drawable.icon_grid_32);
                    mToolBar.getRightButton().setTag(ACTION_LIST);
                    mWareApdater.resetLayout(R.layout.hot_product);
                    mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(WareListActivity.this ,
                            LinearLayoutManager.VERTICAL , false));
                    mRecyclerView.setAdapter(mWareApdater);
                }
            }
        });
    }

    private void initTab()
    {
        TabLayout.Tab tab = mTabLayout.newTab();
        tab.setText("默认");
        tab.setTag(0);
        mTabLayout.addTab(tab);

        tab = mTabLayout.newTab();
        tab.setText("价格");
        tab.setTag(2);
        mTabLayout.addTab(tab);

        tab = mTabLayout.newTab();
        tab.setTag(1);
        tab.setText("销量");
        mTabLayout.addTab(tab);

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                orderBy = (int) tab.getTag();
                currentPage = 1;
                state = STATE_REFRESH;
                getData();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab)
            {
            }
        });
    }

    private void initRefreshLayout()
    {
        mRefreshLayout.setLoadMore(true);
        mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener()
        {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout)
            {
                refreshData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout)
            {
                if (currentPage <= totalPage)
                {
                    loadMoreData();
                }
                else
                {
                    Toast.makeText(WareListActivity.this , "没有下一页数据了" , Toast.LENGTH_SHORT).show();
                    mRefreshLayout.finishRefreshLoadMore();
                }
            }
        });
    }

    private void refreshData()
    {
        currentPage = 1;
        state = STATE_REFRESH;
        getData();
    }

    private void loadMoreData()
    {
        currentPage = currentPage + 1;
        state = STATE_MORE;
        getData();
    }

    private void getData()
    {
        String url = Contants.API.WARE_LIST_URL + "?campaignId=" + campaignId + "&orderBy=" +
                orderBy + "&curPage=" + currentPage + "&pageSize=" + pageSize;

        okHttpHelper.get(url, new DialogCallBack<Page<Wares>>(this) {

            @Override
            public void onSuccess(Response response, Page<Wares> waresPage)
            {
                pageWares = waresPage;
                datas = waresPage.getList();
                currentPage = waresPage.getCurrentPage();
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
                mTxtSummary.setText("共有" + pageWares.getTotalCount() + "件商品");
                mWareApdater = new HWAdapter(WareListActivity.this , datas);
                mRecyclerView.setAdapter(mWareApdater);
                mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mRecyclerView.addItemDecoration(new DividerItemDecoration1(this , DividerItemDecoration1.VERTICAL_LIST));
                break;
            case STATE_REFRESH:
                mWareApdater.clearData();
                mWareApdater.addData(datas);
                mRecyclerView.scrollToPosition(0);
                mRefreshLayout.finishRefresh();
                break;
            case STATE_MORE:
                mWareApdater.addData(mWareApdater.getData().size() , datas);
                mRecyclerView.scrollToPosition(mWareApdater.getData().size());
                mRefreshLayout.finishRefreshLoadMore();
                break;
        }
    }
}
