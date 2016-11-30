package com.test.zhangtao.activitytest.pagers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.test.zhangtao.activitytest.R;

/**
 * Created by zhangtao on 16/11/4.
 */
public class PayResultActivity extends BaseActivity
{
    @ViewInject(R.id.payResult_img)
    private ImageView payResultImage;
    @ViewInject(R.id.payResult_tv)
    private TextView payResultTextview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pay_result);

        ViewUtils.inject(this);
        int status = getIntent().getIntExtra("status" , 3);
        changeUI(status);
    }

    private void changeUI(int status)
    {
        switch (status)
        {
            case 1:
                payResultImage.setImageResource(R.drawable.icon_success_128);
                payResultTextview.setText("支付成功");
                break;
            case -1:
                payResultImage.setImageResource(R.drawable.icon_cancel_128);
                payResultTextview.setText("支付失败");
                break;
            case -2:
                payResultImage.setImageResource(R.drawable.icon_cancel_128);
                payResultTextview.setText("支付取消");
                break;
            default:
                payResultImage.setImageResource(R.drawable.icon_cancel_128);
                payResultTextview.setText("支付非法");
                break;
        }
    }

    @OnClick(R.id.back_to_home)
    public void backHome(View view)
    {
        this.finish();
    }
}
