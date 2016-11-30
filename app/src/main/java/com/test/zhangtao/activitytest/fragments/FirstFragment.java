package com.test.zhangtao.activitytest.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.adapter.DividerItemDecortion;
import com.test.zhangtao.activitytest.adapter.HomeCategoryAdapter;
import com.test.zhangtao.activitytest.bean.Banner;
import com.test.zhangtao.activitytest.bean.Campaign;
import com.test.zhangtao.activitytest.bean.HomeCampaign;
import com.test.zhangtao.activitytest.bean.HomeCategory;
import com.test.zhangtao.activitytest.http.BaseCallBack;
import com.test.zhangtao.activitytest.http.DialogCallBack;
import com.test.zhangtao.activitytest.http.OkHttpHelper;
import com.test.zhangtao.activitytest.pagers.WareListActivity;
import com.test.zhangtao.activitytest.widget.Contants;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangtao on 16/9/25.
 */
public class FirstFragment extends BaseFragment
{
    @ViewInject(R.id.slider)
    private SliderLayout mSliderLayout;
    @ViewInject(R.id.custom_indicator)
    private PagerIndicator indicator;
    @ViewInject(R.id.recyclerView)
    private RecyclerView mRecyclerView;
    private HomeCategoryAdapter mAdapter;
    private List<Banner> mBanner;
    private OkHttpHelper httpHelper = OkHttpHelper.getInstance();

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance)
    {
        return inflater.inflate(R.layout.first_fragment , container , false);
    }

    @Override
    public void init()
    {
        requestImage();
        initRecyclerView();
    }

    private void requestImage()
    {
        String url = "http://112.124.22.238:8081/course_api/banner/query?type=1";

        httpHelper.get(url, new DialogCallBack<List<Banner>>(getActivity()) {

            @Override
            public void onSuccess(Response response, List<Banner> banners)
            {
                mBanner = banners;
                initSlider();
            }

            @Override
            public void onError(Response response, int code, Exception e)
            {
            }
        });
    }

    private void initRecyclerView()
    {
        httpHelper.get(Contants.API.CAMPAIGN_HOME, new BaseCallBack<List<HomeCampaign>>()
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
            public void onResponse(Response response)
            {
            }

            @Override
            public void onSuccess(Response response, List<HomeCampaign> homeCampaigns)
            {
                initData(homeCampaigns);
            }

            @Override
            public void onError(Response response, int code, Exception e)
            {
            }

            @Override
            public void onTokenError(Response response, int code)
            {
            }
        });
    }

    private void initData(List<HomeCampaign> homeCampaigns)
    {
        mAdapter = new HomeCategoryAdapter(homeCampaigns , getActivity());
        mAdapter.setOnCampaignClickListener(new HomeCategoryAdapter.OnCampaignClickListener()
        {
            @Override
            public void onClick(View view, Campaign campaign)
            {
                Intent intent = new Intent(getActivity() , WareListActivity.class);
                intent.putExtra(Contants.CAMPAIGN_ID , campaign.getId());
                startActivity(intent);
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecortion());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
    }

    private void initSlider()
    {
        if (mBanner != null)
        {
            for (final Banner banner : mBanner)
            {
                TextSliderView textSliderView = new TextSliderView(this.getActivity());
                textSliderView.image(banner.getImgUrl());
                textSliderView.description(banner.getName());
                mSliderLayout.addSlider(textSliderView);
                textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener()
                {
                    @Override
                    public void onSliderClick(BaseSliderView slider)
                    {
                        Toast.makeText(FirstFragment.this.getActivity() , banner.getName() , Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        mSliderLayout.setCustomIndicator(indicator);
        mSliderLayout.setCustomAnimation(new DescriptionAnimation());
        mSliderLayout.setPresetTransformer(SliderLayout.Transformer.Tablet);
        mSliderLayout.setDuration(3000);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mSliderLayout.stopAutoCycle();
    }
}
