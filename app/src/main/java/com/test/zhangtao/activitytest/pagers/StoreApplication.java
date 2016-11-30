package com.test.zhangtao.activitytest.pagers;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.test.zhangtao.activitytest.bean.User;
import com.test.zhangtao.activitytest.util.UserLocalData;

/**
 * Created by zhangtao on 16/9/30.
 */
public class StoreApplication extends Application
{
    private User user;
    private static StoreApplication mInstance;
    private Intent intent;

    public static StoreApplication getInstance()
    {
        return mInstance;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        mInstance = this;
        initUser();
        Fresco.initialize(this);
    }

    private void initUser()
    {
        this.user = UserLocalData.getUser(this);
    }

    public User getUser()
    {
        return user;
    }

    public String getToken()
    {
        return UserLocalData.getToken(this);
    }

    public void putUser(User user , String token)
    {
        this.user = user;
        UserLocalData.putUser(this , user);
        UserLocalData.putToken(this , token);
    }

    public void clearUser()
    {
        this.user = null;
        UserLocalData.clearUser(this);
        UserLocalData.clearToken(this);
    }

    public void putIntent(Intent intent)
    {
        this.intent = intent;
    }

    public Intent getIntent()
    {
        return intent;
    }

    public void jumpToTargetActivity(Context context)
    {
        context.startActivity(intent);
        this.intent = null;
    }
}
