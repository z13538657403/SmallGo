package com.test.zhangtao.activitytest.pagers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Response;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.adapter.MyOrderAdapter;
import com.test.zhangtao.activitytest.bean.Order;
import com.test.zhangtao.activitytest.decoration.CardViewtemDecortion;
import com.test.zhangtao.activitytest.http.OkHttpHelper;
import com.test.zhangtao.activitytest.http.SimpleCallBack;
import com.test.zhangtao.activitytest.widget.CnToolBar;
import com.test.zhangtao.activitytest.widget.Contants;
import com.test.zhangtao.activitytest.widget.WrapContentLinearLayoutManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangtao on 16/11/6.
 */
public class MyOrderActivity extends BaseActivity implements TabLayout.OnTabSelectedListener
{
    public static final int STATUS_ALL = 1000;
    public static final int STATUS_SUCCESS = 1;     //支付成功的订单
    public static final int STATUS_PAY_FALL = -2;   //支付失败的订单
    public static final int STATUS_PAY_WAIT = 0;    //待支付的订单
    private int status = STATUS_ALL;

    @ViewInject(R.id.myOrder_toolbar)
    private CnToolBar mToolbar;

    @ViewInject(R.id.order_tab_layout)
    private TabLayout mTabLayout;

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerview;

    private MyOrderAdapter mAdapter;
    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my_order);
        ViewUtils.inject(this);

        initToolBar();
        initTab();
        getOrders();
    }

    private void initToolBar()
    {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
    }

    private void initTab()
    {
        TabLayout.Tab tab = mTabLayout.newTab();
        tab.setText("全部");
        tab.setTag(STATUS_ALL);
        mTabLayout.addTab(tab);

        tab = mTabLayout.newTab();
        tab.setText("支付成功");
        tab.setTag(STATUS_SUCCESS);
        mTabLayout.addTab(tab);

        tab = mTabLayout.newTab();
        tab.setText("待支付");
        tab.setTag(STATUS_PAY_WAIT);
        mTabLayout.addTab(tab);

        tab = mTabLayout.newTab();
        tab.setText("支付失败");
        tab.setTag(STATUS_PAY_FALL);
        mTabLayout.addTab(tab);

        mTabLayout.setOnTabSelectedListener(this);
    }

    private void getOrders()
    {
        Long userId = StoreApplication.getInstance().getUser().getId();
        Map<String , Object> params = new HashMap<>();

        params.put("user_id" , userId);
        params.put("status" , status);

        okHttpHelper.get(Contants.ORDER_LIST, params, new SimpleCallBack<List<Order>>(this)
        {
            @Override
            public void onSuccess(Response response, List<Order> orders)
            {
                showOrders(orders);
            }

            @Override
            public void onError(Response response, int code, Exception e)
            {
            }
        });
    }

    private void showOrders(List<Order> orders)
    {
        if (mAdapter == null)
        {
            mAdapter = new MyOrderAdapter(this , orders);
            mRecyclerview.setAdapter(mAdapter);
            mRecyclerview.setLayoutManager(new WrapContentLinearLayoutManager(this , LinearLayoutManager.VERTICAL , false));
            mRecyclerview.addItemDecoration(new CardViewtemDecortion());
        }
        else
        {
            mAdapter.clearData();
            mAdapter.addData(orders);
            mRecyclerview.setAdapter(mAdapter);
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab)
    {
        status = (int) tab.getTag();
        getOrders();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab)
    {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab)
    {
    }
}
