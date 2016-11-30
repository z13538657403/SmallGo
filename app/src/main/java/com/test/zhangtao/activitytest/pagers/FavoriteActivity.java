package com.test.zhangtao.activitytest.pagers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Response;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.adapter.FavoriteAdapter;
import com.test.zhangtao.activitytest.bean.Favorites;
import com.test.zhangtao.activitytest.bean.ShoppingCar;
import com.test.zhangtao.activitytest.bean.User;
import com.test.zhangtao.activitytest.decoration.CardViewtemDecortion;
import com.test.zhangtao.activitytest.http.OkHttpHelper;
import com.test.zhangtao.activitytest.http.SimpleCallBack;
import com.test.zhangtao.activitytest.msg.BaseRespMsg;
import com.test.zhangtao.activitytest.util.CarProvider;
import com.test.zhangtao.activitytest.widget.CnToolBar;
import com.test.zhangtao.activitytest.widget.Contants;
import com.test.zhangtao.activitytest.widget.WrapContentLinearLayoutManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangtao on 16/11/7.
 */
public class FavoriteActivity extends BaseActivity
{
    @ViewInject(R.id.favorite_toolbar)
    private CnToolBar mToolBar;
    @ViewInject(R.id.favorite_recyclerView)
    private RecyclerView mRecyclerView;

    private CarProvider cartProvider;
    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();
    private FavoriteAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_favorite_list);
        ViewUtils.inject(this);

        cartProvider = CarProvider.getInstance();
        cartProvider.setMyConext(this);

        initToolBar();
        initData();
    }

    private void initToolBar()
    {
        mToolBar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FavoriteActivity.this.finish();
            }
        });
    }

    private void initData()
    {
        User user = StoreApplication.getInstance().getUser();
        Map<String , Object> params = new HashMap<>(1);
        params.put("user_id" , user.getId() + "");
        okHttpHelper.get(Contants.FAVORITE_LIST, params, new SimpleCallBack<List<Favorites>>(this)
        {
            @Override
            public void onSuccess(Response response, List<Favorites> favorites)
            {
                showData(favorites);
            }

            @Override
            public void onError(Response response, int code, Exception e)
            {
            }
        });

    }

    private void showData(List<Favorites> favorites)
    {
        if (mAdapter == null)
        {
            mAdapter = new FavoriteAdapter(this, favorites, new FavoriteAdapter.FavoriteListener()
            {
                @Override
                public void removeFavorite(long favorite_id)
                {
                    toDeleteFavorite(favorite_id);
                }

                @Override
                public void addToFavorite(ShoppingCar shoppingCar)
                {
                    cartProvider.put(shoppingCar);
                    Toast.makeText(FavoriteActivity.this , "已经添加到购物车！！！" , Toast.LENGTH_SHORT).show();
                }
            });
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this , LinearLayoutManager.VERTICAL , false));
            mRecyclerView.addItemDecoration(new CardViewtemDecortion());
        }
        else
        {
            mAdapter.clearData();
            mAdapter.addData(favorites);
            mRecyclerView.scrollToPosition(0);
        }
    }

    private void toDeleteFavorite(long favorite_id)
    {
        Map<String , Object> params = new HashMap<>(1);
        params.put("id" , favorite_id + "");

        okHttpHelper.post(Contants.FAVORITE_DEL, params, new SimpleCallBack<BaseRespMsg>(this)
        {
            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg)
            {
                if (baseRespMsg.getStatus() == BaseRespMsg.STATUS_SUCCESS)
                    initData();
            }

            @Override
            public void onError(Response response, int code, Exception e)
            {
            }
        });
    }
}
