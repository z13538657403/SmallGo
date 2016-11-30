package com.test.zhangtao.activitytest.util;

import android.os.CountDownTimer;
import android.widget.TextView;

import com.test.zhangtao.activitytest.R;

/**
 * Created by zhangtao on 16/11/2.
 */
public class CountTimerView extends CountDownTimer
{
    //时间防止从59s开始显示
    public static final int TIME_COUNT = 61000;
    private TextView btn;
    private int endStrRid;

    /**
     *  millisInFuture 倒计时总时间
     *  countDownInterval 渐变时间
     *  btn 点击的按钮(因为Button是TextView子类，为了通用设置为TextView)
     *  endStrRid  倒计时结束后，按钮对应显示的文字
     */
    public CountTimerView(long millisInFuture, long countDownInterval , TextView btn , int endStrRid)
    {
        super(millisInFuture, countDownInterval);
        this.btn = btn;
        this.endStrRid = endStrRid;
    }

    public CountTimerView(TextView btn , int endStrRid)
    {
        super(TIME_COUNT , 1000);
        this.btn = btn;
        this.endStrRid = endStrRid;
    }

    public CountTimerView(TextView btn)
    {
        super(TIME_COUNT , 1000);
        this.btn = btn;
        this.endStrRid = R.string.smssdk_resend_identify_code;
    }

    //计时完毕时触发
    @Override
    public void onFinish()
    {
        btn.setText(endStrRid);
        btn.setEnabled(true);
    }


    //计时过程显示
    @Override
    public void onTick(long millisUntilFinished)
    {
        btn.setEnabled(false);
        btn.setText(millisUntilFinished / 1000 + "秒后可重新发送");
    }
}
