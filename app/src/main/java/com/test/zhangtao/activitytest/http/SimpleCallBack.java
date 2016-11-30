package com.test.zhangtao.activitytest.http;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.test.zhangtao.activitytest.pagers.LoginActivity;
import com.test.zhangtao.activitytest.pagers.StoreApplication;

import java.io.IOException;

/**
 * Created by zhangtao on 16/11/1.
 */
public abstract class SimpleCallBack<T> extends BaseCallBack<T>
{
    protected Context mContext;
    public SimpleCallBack(Context context)
    {
        mContext = context;
    }

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
    public void onTokenError(Response response, int code)
    {
        Toast.makeText(mContext , "请登录！！！"  , Toast.LENGTH_SHORT).show();

        Intent intent = new Intent();
        intent.setClass(mContext , LoginActivity.class);
        mContext.startActivity(intent);

        StoreApplication.getInstance().clearUser();
    }
}
