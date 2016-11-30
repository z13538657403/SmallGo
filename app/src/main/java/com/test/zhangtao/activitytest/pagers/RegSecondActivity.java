package com.test.zhangtao.activitytest.pagers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.okhttp.Response;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.bean.User;
import com.test.zhangtao.activitytest.http.DialogCallBack;
import com.test.zhangtao.activitytest.http.OkHttpHelper;
import com.test.zhangtao.activitytest.msg.LoginRespMsg;
import com.test.zhangtao.activitytest.util.CountTimerView;
import com.test.zhangtao.activitytest.util.DESUtil;
import com.test.zhangtao.activitytest.widget.ClearEditText;
import com.test.zhangtao.activitytest.widget.CnToolBar;
import com.test.zhangtao.activitytest.widget.Contants;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;

/**
 * Created by zhangtao on 16/11/2.
 */
public class RegSecondActivity extends BaseActivity
{
    @ViewInject(R.id.register_second_toolbar)
    private CnToolBar mToolBar;

    @ViewInject(R.id.txtTip)
    private TextView mTxtTip;

    @ViewInject(R.id.btn_reSend)
    private Button mBtnResend;

    @ViewInject(R.id.editTxt_code)
    private ClearEditText mEtCode;

    private String phone;
    private String pwd;
    private String countryCode;

    private CountTimerView countTimerView;
    private ProgressDialog dialog;
    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();
    private SMSEventHandler eventHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register_second);
        ViewUtils.inject(this);

        initToolBar();

        phone = getIntent().getStringExtra("phone");
        pwd = getIntent().getStringExtra("pwd");
        countryCode = getIntent().getStringExtra("countryCode");
        Log.d("VVVV" , phone + "----" + pwd + "---" + countryCode);

        String formatePhone = "+" + countryCode + " " + splitPhoneNum(phone);
        String text = getString(R.string.smssdk_send_mobile_detail) + formatePhone;
        mTxtTip.setText(Html.fromHtml(text));

        CountTimerView timerView = new CountTimerView(mBtnResend);
        timerView.start();

        SMSSDK.initSDK(this , Contants.APP_KEY , Contants.APP_SECRET);
        eventHandler = new SMSEventHandler();
        SMSSDK.registerEventHandler(eventHandler);

        dialog = new ProgressDialog(this);
        dialog.setMessage("正在校验验证码");
    }

    private void initToolBar()
    {
        mToolBar.setRightButtonOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                submitCode();
            }
        });
    }

    @OnClick(R.id.btn_reSend)
    public void reSendCode(View view)
    {
        SMSSDK.getVerificationCode("+" + countryCode , phone);
        countTimerView = new CountTimerView(mBtnResend , R.string.smssdk_resend_identify_code);
        countTimerView.start();
    }

    private void submitCode()
    {
        String vCode = mEtCode.getText().toString().trim();
        if (TextUtils.isEmpty(vCode))
        {
            Toast.makeText(this , R.string.smssdk_write_identify_code , Toast.LENGTH_SHORT).show();
            return;
        }
        SMSSDK.submitVerificationCode(countryCode , phone , vCode);
    }

    //分割电话号码
    private String splitPhoneNum(String phone)
    {
        StringBuilder builder = new StringBuilder(phone);
        builder.reverse();
        for (int i = 4 , len = builder.length() ; i < len ; i += 5)
        {
            builder.insert(i , "  ");
        }
        builder.reverse();
        return builder.toString();
    }

    class SMSEventHandler extends EventHandler
    {
        @Override
        public void afterEvent(final int event, final int result, final Object data)
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    if (dialog != null && dialog.isShowing())
                    {
                        dialog.dismiss();
                    }
                    if (result == SMSSDK.RESULT_COMPLETE)
                    {
                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE)
                        {
                            deReg();
                        }
                    }
                    else
                    {
                        try {
                            //根据服务器返回的网络错误，给Toast提示
                            ((Throwable) data).printStackTrace();
                            Throwable throwable = (Throwable) data;
                            JSONObject object = new JSONObject(throwable.getMessage());
                            String des = object.optString("detail");
                            if (!TextUtils.isEmpty(des))
                            {
                                return;
                            }
                        }
                        catch (Exception e)
                        {
                            SMSLog.getInstance().w(e);
                        }
                    }
                }
            });
        }
    }

    private void deReg()
    {
        Map<String , Object> params = new HashMap<>(2);
        params.put("phone" , phone);
        params.put("password" , DESUtil.encode(Contants.DES_KEY , pwd));
        okHttpHelper.post(Contants.USER_REG, params, new DialogCallBack<LoginRespMsg<User>>(this)
        {
            @Override
            public void onSuccess(Response response, LoginRespMsg<User> userLoginRespMsg)
            {
                Log.d("Status" , userLoginRespMsg.getStatus() + "");
                if (userLoginRespMsg.getStatus() == LoginRespMsg.STATUS_ERROR)
                {
                    Toast.makeText(RegSecondActivity.this , "这个手机号码已经注册过了，请换一个手机号码！！！" , Toast.LENGTH_SHORT).show();
                    return;
                }
                StoreApplication application = StoreApplication.getInstance();
                application.putUser(userLoginRespMsg.getData() , userLoginRespMsg.getToken());
                startActivity(new Intent(RegSecondActivity.this , MainActivity.class));
                finish();
            }

            @Override
            public void onError(Response response, int code, Exception e)
            {
            }

            @Override
            public void onTokenError(Response response, int code)
            {
                super.onTokenError(response, code);
            }

        });
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }
}
