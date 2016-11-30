package com.test.zhangtao.activitytest.http;

import android.app.ProgressDialog;
import android.content.Context;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

/**
 * Created by zhangtao on 16/11/1.
 */
public abstract class DialogCallBack<T> extends SimpleCallBack<T>
{
    private ProgressDialog progressDialog;

    public DialogCallBack(Context context)
    {
        super(context);
        initProgressDialog();
    }

    @Override
    public void onRequestBefore(Request request)
    {
        showDialog();
    }

    @Override
    public void onResponse(Response response)
    {
        dismissDialog();
    }

    private void initProgressDialog()
    {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("正在加载中");
    }

    public void showDialog()
    {
        progressDialog.show();
    }

    public void dismissDialog()
    {
        progressDialog.dismiss();
    }
}
