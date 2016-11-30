package com.test.zhangtao.activitytest.pagers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.widget.ClearEditText;
import com.test.zhangtao.activitytest.widget.CnToolBar;
import com.test.zhangtao.activitytest.widget.Contants;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;

/**
 * Created by zhangtao on 16/11/2.
 */
public class RegisterActivity extends BaseActivity
{
    private static final String TAG = "RegisterActivity";
    //默认使用中国区号
    private static final String DEFAULT_COUNTRY_ID = "42";

    @ViewInject(R.id.register_toolbar)
    private CnToolBar mToolBar;

    @ViewInject(R.id.txtCountry)
    private TextView mTxtCountry;

    @ViewInject(R.id.txtCountryCode)
    private TextView mTxtCountryCode;

    @ViewInject(R.id.editTxt_phone)
    private ClearEditText mEtxtPhone;

    @ViewInject(R.id.editTxt_pwd)
    private ClearEditText mEtxtPwd;

    private SMSEventHandler eventHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        ViewUtils.inject(this);

        initToolBar();

        SMSSDK.initSDK(this , Contants.APP_KEY , Contants.APP_SECRET);
        eventHandler = new SMSEventHandler();
        SMSSDK.registerEventHandler(eventHandler);
    }

    class SMSEventHandler extends EventHandler
    {
        @Override
        public void afterEvent(final int event, final int result, final Object data)
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (result == SMSSDK.RESULT_COMPLETE)
                    {
                        if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES)
                        {
                            onCountryListGot((ArrayList<HashMap<String , Object>>) data);
                        }
                        else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE)
                        {
                            //请求验证码后，跳转到验证码填写界面
                            Toast.makeText(RegisterActivity.this , "验证码已经发送到您的手机，请查收！！！" , Toast.LENGTH_SHORT).show();
                            afterVertificationCodeRequested((Boolean)data);
                        }
                        else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE)
                        {
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
                                Toast.makeText(RegisterActivity.this , des , Toast.LENGTH_SHORT).show();
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

    private void initToolBar()
    {
        mToolBar.setRightButtonOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getCode();
            }
        });

        mToolBar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                RegisterActivity.this.finish();
            }
        });
    }

    private void getCode()
    {
        String phone = mEtxtPhone.getText().toString().trim().replaceAll("\\s*" , "");
        String code = mTxtCountryCode.getText().toString().trim();
        String pwd = mEtxtPwd.getText().toString().trim();

        checkPhoneNum(phone , code);
        Log.d("Register", code + "  "+ phone);
        SMSSDK.getVerificationCode(code , phone);
    }

    private void checkPhoneNum(String phone, String code)
    {
        if (code.startsWith("+"))
        {
            code = code.substring(1);
        }

        if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this , "请输入手机号码" , Toast.LENGTH_SHORT).show();
            return;
        }
        if (code == "86")
        {
            if (phone.length() != 11)
            {
                Toast.makeText(this , "手机号码长度不对" , Toast.LENGTH_SHORT).show();
                return;
            }
        }

        String rule = "^1(3|5|7|8|4)\\d{9}";
        Pattern p = Pattern.compile(rule);
        Matcher m = p.matcher(phone);
        if (!m.matches())
        {
            Toast.makeText(this , "您输入的手机号码格式不正确" , Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void onCountryListGot(ArrayList<HashMap<String, Object>> countries)
    {
        //解析国家列表
        for (HashMap<String , Object> country : countries)
        {
            String code = (String) country.get("zone");
            String rule = (String) country.get("rule");
            if (TextUtils.isEmpty(code) || TextUtils.isEmpty(rule))
            {
                continue;
            }
            Log.d(TAG , "code = " + code + " rule = " + rule);
        }
    }

    private void afterVertificationCodeRequested(Boolean data)
    {
        String phone = mEtxtPhone.getText().toString().replaceAll("\\s*" , "");
        String code = mTxtCountryCode.getText().toString().trim();
        String pwd = mEtxtPwd.getText().toString().trim();
        if (code.startsWith("+"))
        {
            code = code.substring(1);
        }

        Intent intent = new Intent(this , RegSecondActivity.class);
        intent.putExtra("phone" , phone);
        intent.putExtra("pwd" , pwd);
        intent.putExtra("countryCode" , code);
        startActivity(intent);
    }

    private String[] getCurrentCountry()
    {
        String mcc = getMCC();
        String[] country = null;
        if (!TextUtils.isEmpty(mcc))
        {
            country = SMSSDK.getCountryByMCC(mcc);
        }
        if (country == null)
        {
            Log.w("SMSSDK" , "no country found by MCC" + mcc);
            country = SMSSDK.getCountry(DEFAULT_COUNTRY_ID);
        }
        return country;
    }

    private String getMCC()
    {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //返回当前手机注册的运营商所在国家的MCC＋MNC，如果没注册网络就为空
        String networkOperator = tm.getNetworkOperator();
        if (!TextUtils.isEmpty(networkOperator))
        {
            return networkOperator;
        }
        //返回SIM卡运营商所在国家的MCC＋MNC，5位或6位，如果没有SIM卡返回空
        return tm.getSimOperator();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }
}
