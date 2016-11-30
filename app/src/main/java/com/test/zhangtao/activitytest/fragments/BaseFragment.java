package com.test.zhangtao.activitytest.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.lidroid.xutils.ViewUtils;
import com.test.zhangtao.activitytest.bean.User;
import com.test.zhangtao.activitytest.pagers.LoginActivity;
import com.test.zhangtao.activitytest.pagers.StoreApplication;

/**
 * Created by zhangtao on 16/11/2.
 */
public abstract class BaseFragment extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = createView(inflater , container , savedInstanceState);
        ViewUtils.inject(this , view);
        initToolBar();
        init();
        return view;
    }

    public void initToolBar()
    {
    }

    public abstract View createView(LayoutInflater inflater , ViewGroup container , Bundle savedInstance);

    public abstract void init();

    public void startActivity(Intent intent , boolean isNeedLogin)
    {
        if (isNeedLogin)
        {
            User user = StoreApplication.getInstance().getUser();
            if (user != null)
            {
                super.startActivity(intent);
            }
            else
            {
                StoreApplication.getInstance().putIntent(intent);
                Intent loginIntent = new Intent(getActivity() , LoginActivity.class);
                super.startActivity(loginIntent);
            }
        }
        else
        {
            super.startActivity(intent);
        }
    }
}
