package com.test.zhangtao.activitytest.pagers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.test.zhangtao.activitytest.bean.User;

/**
 * Created by zhangtao on 16/11/2.
 */
public class BaseActivity extends AppCompatActivity
{
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
                Intent loginIntent = new Intent(this , LoginActivity.class);
                super.startActivity(loginIntent);
            }
        }
        else
        {
            super.startActivity(intent);
        }
    }
}
