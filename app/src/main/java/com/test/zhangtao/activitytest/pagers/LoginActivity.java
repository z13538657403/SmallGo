package com.test.zhangtao.activitytest.pagers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
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
import com.test.zhangtao.activitytest.msg.BaseRespMsg;
import com.test.zhangtao.activitytest.msg.LoginRespMsg;
import com.test.zhangtao.activitytest.util.DESUtil;
import com.test.zhangtao.activitytest.widget.ClearEditText;
import com.test.zhangtao.activitytest.widget.CnToolBar;
import com.test.zhangtao.activitytest.widget.Contants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangtao on 16/11/1.
 */
public class LoginActivity extends BaseActivity
{
    @ViewInject(R.id.login_toolBar)
    private CnToolBar mToolBar;
    @ViewInject(R.id.user_phone)
    private ClearEditText mPhoneNumber;
    @ViewInject(R.id.user_password)
    private ClearEditText mPassWord;

    private OkHttpHelper okHttpHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        ViewUtils.inject(LoginActivity.this);
        okHttpHelper = OkHttpHelper.getInstance();
        initToolBar();
    }

    private void initToolBar()
    {
        mToolBar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                LoginActivity.this.finish();
            }
        });
    }

    @OnClick(R.id.btn_login)
    public void login(View view)
    {
        String phone = mPhoneNumber.getText().toString().trim();
        if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(LoginActivity.this , "请输入手机号码" , Toast.LENGTH_SHORT).show();
            return;
        }

        String password = mPassWord.getText().toString().trim();
        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(LoginActivity.this , "请输入密码" , Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String , Object> params = new HashMap<>(2);
        params.put("phone" , phone);
        params.put("password" , DESUtil.encode(Contants.DES_KEY , password));

        okHttpHelper.post(Contants.LOGIN_URL, params, new DialogCallBack<LoginRespMsg<User>>(this) {

            @Override
            public void onSuccess(Response response, LoginRespMsg<User> userLoginRespMsg)
            {
                if (userLoginRespMsg.getStatus() == BaseRespMsg.STATUS_ERROR)
                {
                    Toast.makeText(LoginActivity.this , "请输入正确手机号码和密码" , Toast.LENGTH_SHORT).show();
                    return;
                }

                StoreApplication application = StoreApplication.getInstance();
                application.putUser(userLoginRespMsg.getData() , userLoginRespMsg.getToken());
                if (application.getIntent() == null)
                {
                    setResult(RESULT_OK);
                    finish();
                }
                else
                {
                    application.jumpToTargetActivity(LoginActivity.this);
                    finish();
                }
            }

            @Override
            public void onError(Response response, int code, Exception e)
            {
            }
        });
    }

    @OnClick(R.id.txt_toReg)
    public void toRegister(View view)
    {
        Intent intent = new Intent(this , RegisterActivity.class);
        startActivity(intent);
    }
}
