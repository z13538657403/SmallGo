package com.test.zhangtao.activitytest.pagers;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.test.zhangtao.activitytest.R;

/**
 * Created by zhangtao on 16/11/7.
 */
public class WelcomeActivity extends BaseActivity
{
    @ViewInject(R.id.welcome_image)
    private ImageView welcomeImage;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);
        ViewUtils.inject(this);

//        new Handler().postDelayed(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                jump();
//            }
//        } , 3500);

        initView();
    }

    private void initView()
    {
        AlphaAnimation animation = new AlphaAnimation(0.2f , 1.0f);
        animation.setDuration(3000);
        animation.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {
            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                jump();
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {
            }
        });
        welcomeImage.startAnimation(animation);
    }

    private void jump()
    {
        Intent intent = new Intent(WelcomeActivity.this , MainActivity.class);
        startActivity(intent);
        finish();
    }
}
