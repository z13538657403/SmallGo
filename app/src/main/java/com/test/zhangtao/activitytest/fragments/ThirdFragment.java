package com.test.zhangtao.activitytest.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.adapter.BaseAdapter;
import com.test.zhangtao.activitytest.adapter.CategoryAdapter;
import com.test.zhangtao.activitytest.adapter.HWAdapter;
import com.test.zhangtao.activitytest.adapter.WaresAdapter;
import com.test.zhangtao.activitytest.bean.Banner;
import com.test.zhangtao.activitytest.bean.Page;
import com.test.zhangtao.activitytest.bean.ProductCategory;
import com.test.zhangtao.activitytest.bean.Wares;
import com.test.zhangtao.activitytest.decoration.DividerGridItemDecoration;
import com.test.zhangtao.activitytest.decoration.DividerItemDecoration1;
import com.test.zhangtao.activitytest.http.BaseCallBack;
import com.test.zhangtao.activitytest.http.DialogCallBack;
import com.test.zhangtao.activitytest.http.OkHttpHelper;
import com.test.zhangtao.activitytest.http.SimpleCallBack;
import com.test.zhangtao.activitytest.widget.Contants;

import java.io.IOException;
import java.util.List;

/**
 * Created by zhangtao on 16/9/25.
 */
public class ThirdFragment extends BaseFragment
{
    @ViewInject(R.id.recyclerView_category)
    private RecyclerView categoryList;
    @ViewInject(R.id.category_slider)
    private SliderLayout mSliderLayout;
    @ViewInject(R.id.refresh_layout)
    private MaterialRefreshLayout mRefreshLayout;
    @ViewInject(R.id.recyclerView_wares)
    private RecyclerView mRecyclerView;
    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    private List<ProductCategory> listData;
    private List<Banner> bannerData;

    private WaresAdapter mWaresAdapter;
    private CategoryAdapter categoryAdapter;

    private int currPage = 1;
    private int totalPage = 1;
    private int pageSize = 10;
    private long category_id = 0;

    private static final int STATE_NORMAL= 0;
    private static final int STATE_REFRESH = 1;
    private static final int STATE_MORE = 2;
    private int state = STATE_NORMAL;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance)
    {
        return inflater.inflate(R.layout.third_fragment , container , false);
    }

    @Override
    public void init()
    {
        initRefreshLayout();
        initListData();
        initSliderImage();
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
                if (currPage <= totalPage)
                {
                    loadMoreData();
                }
                else
                {
                    Toast.makeText(getContext() , "没有下一页数据了" , Toast.LENGTH_SHORT).show();
                    mRefreshLayout.finishRefreshLoadMore();
                }
            }
        });
    }

    private void refreshData()
    {
        currPage = 1;
        state = STATE_REFRESH;
        requestWares(category_id);
    }

    private void loadMoreData()
    {
        currPage = ++ currPage;
        state = STATE_MORE;
        requestWares(category_id);
    }

    private void initSliderImage()
    {
        okHttpHelper.get(Contants.API.SLIDER_IMAGE, new SimpleCallBack<List<Banner>>(getActivity())
        {
            @Override
            public void onSuccess(Response response, List<Banner> banners)
            {
                bannerData = banners;
                showSlider();
            }

            @Override
            public void onError(Response response, int code, Exception e)
            {
            }
        });
    }

    private void showSlider()
    {
        if (bannerData != null)
        {
            for (final Banner banner : bannerData)
            {
                TextSliderView sliderView = new TextSliderView(getContext());
                sliderView.image(banner.getImgUrl());
                sliderView.description(banner.getName());
                mSliderLayout.addSlider(sliderView);
                sliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener()
                {
                    @Override
                    public void onSliderClick(BaseSliderView slider)
                    {
                        Toast.makeText(getContext() , banner.getName() + "" , Toast.LENGTH_SHORT).show();
                    }
                });
            }

            mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            mSliderLayout.setCustomAnimation(new DescriptionAnimation());
            mSliderLayout.setPresetTransformer(SliderLayout.Transformer.ZoomOutSlide);
            mSliderLayout.setDuration(3000);
        }
    }

    private void initListData()
    {
        okHttpHelper.get(Contants.API.CATEGORY_LIST, new BaseCallBack<List<ProductCategory>>()
        {
            @Override
            public void onRequestBefore(Request request)
            {
            }

            @Override
            public void onFailure(Request request, IOException e)
            {
            }

            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onSuccess(Response response, List<ProductCategory> productCategories)
            {
                listData = productCategories;
                initCategoryList();

                if (productCategories != null && productCategories.size() > 0)
                {
                    category_id = productCategories.get(0).getId();
                    requestWares(category_id);
                }
            }

            @Override
            public void onError(Response response, int code, Exception e)
            {
            }

            @Override
            public void onTokenError(Response response, int code) {

            }
        });
    }

    private void initCategoryList()
    {
        categoryAdapter = new CategoryAdapter(getContext() , listData);
        categoryAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener()
        {
            @Override
            public void onClick(View view, int position)
            {
                ProductCategory productCategory = categoryAdapter.getItem(position);
                category_id = productCategory.getId();
                currPage = 1;
                state = STATE_NORMAL;
                requestWares(category_id);
            }
        });
        categoryList.setAdapter(categoryAdapter);
        categoryList.setItemAnimator(new DefaultItemAnimator());
        categoryList.setLayoutManager(new LinearLayoutManager(getContext()));
        categoryList.addItemDecoration(new DividerItemDecoration1(getContext() , DividerItemDecoration1.VERTICAL_LIST));
    }

    private void requestWares(long categoryId)
    {
        String url = Contants.API.CATEGORY_WARES + "?categoryId=" + categoryId + "&curPage=" + currPage + "&pageSize=" + pageSize;
        okHttpHelper.get(url, new DialogCallBack<Page<Wares>>(getActivity())
        {

            @Override
            public void onSuccess(Response response, Page<Wares> waresPage)
            {
                currPage = waresPage.getCurrentPage();
                totalPage = waresPage.getTotalPage();
                showWaresData(waresPage.getList());
            }

            @Override
            public void onError(Response response, int code, Exception e)
            {
            }
        });
    }

    private void showWaresData(List<Wares> waresData)
    {
        switch (state)
        {
            case STATE_NORMAL:
                mWaresAdapter = new WaresAdapter(getContext() , waresData);
                mRecyclerView.setAdapter(mWaresAdapter);

                mRecyclerView.setLayoutManager(new GridLayoutManager(getContext() , 2));
                mRecyclerView.addItemDecoration(new DividerGridItemDecoration(getContext()));
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                break;
            case STATE_REFRESH:
                mWaresAdapter.clearData();
                mWaresAdapter.addData(waresData);

                mRecyclerView.scrollToPosition(0);
                mRefreshLayout.finishRefresh();
                break;
            case STATE_MORE:
                mWaresAdapter.addData(mWaresAdapter.getData().size() , waresData);
                mRecyclerView.scrollToPosition(mWaresAdapter.getData().size());
                mRefreshLayout.finishRefreshLoadMore();
                break;
        }
    }
}
