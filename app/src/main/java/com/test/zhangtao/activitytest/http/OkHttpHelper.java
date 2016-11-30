package com.test.zhangtao.activitytest.http;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.test.zhangtao.activitytest.pagers.StoreApplication;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhangtao on 16/9/29.
 */
public class OkHttpHelper
{
    public static final int TOKEN_MISSING = 401;    //  token丢失
    public static final int TOKEN_ERROR = 402;    //  token错误
    public static final int TOKEN_EXPIRE = 403;   //  token过期

    private static OkHttpClient okHttpClient;
    private Gson gson;
    private Handler handler;

    private OkHttpHelper()
    {
        okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(10 , TimeUnit.SECONDS);
        okHttpClient.setWriteTimeout(10 , TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(10 , TimeUnit.SECONDS);
        gson = new Gson();
        handler = new Handler(Looper.getMainLooper());
    }

    public static OkHttpHelper getInstance()
    {
        return new OkHttpHelper();
    }

    public void get(String url , BaseCallBack callBack)
    {
        get(url , null , callBack);
    }

    public void get(String url , Map<String , Object> params , BaseCallBack callBack)
    {
        Request request = buildGetRequest(url , params);
        doRequest(request , callBack);
    }

    public void post(String url , Map<String , Object> params , BaseCallBack callBack)
    {
        Request request = buildPostRequest(url , params);
        doRequest(request , callBack);
    }

    public void doRequest(final Request request , final BaseCallBack callBack)
    {
        callBack.onRequestBefore(request);

        okHttpClient.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onFailure(Request request, IOException e)
            {
                callBack.onFailure(request , e);
            }

            @Override
            public void onResponse(Response response) throws IOException
            {
                callbackResponse(callBack , response);
                if (response.isSuccessful())
                {
                    String resultStr = response.body().string();
                    if (callBack.mType == String.class)
                    {
                        callbackSuccess(callBack , response , resultStr);
                    }
                    else
                    {
                        try
                        {
                            Object object = gson.fromJson(resultStr , callBack.mType);
                            callbackSuccess(callBack , response , object);
                        }
                        catch (JsonParseException e)
                        {
                            callbackError(callBack , response , response.code() , e);
                        }
                    }
                }
                else if(response.code() == TOKEN_MISSING || response.code() == TOKEN_ERROR || response.code() == TOKEN_EXPIRE)
                {
                    callbackTokenError(callBack , response);
                }
                else
                {
                    callbackError(callBack , response , response.code() , null);
                }
            }
        });
    }

    private void callbackResponse(final BaseCallBack callBack , final Response response)
    {
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                callBack.onResponse(response);
            }
        });
    }

    private void callbackSuccess(final BaseCallBack callBack , final Response response , final Object object)
    {
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                callBack.onSuccess(response , object);
            }
        });
    }

    private void callbackTokenError(final BaseCallBack callBack , final Response response)
    {
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                callBack.onTokenError(response , response.code());
            }
        });
    }

    private void callbackError(final BaseCallBack callBack , final Response response , final int code , final Exception e)
    {
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                callBack.onError(response , code , e);
            }
        });
    }

    private Request buildPostRequest(String url , Map<String , Object> params)
    {
        return buildRequest(url , params , HttpMethodType.POST);
    }

    private Request buildGetRequest(String url , Map<String , Object> params)
    {
        return buildRequest(url , params , HttpMethodType.GET);
    }

    private Request buildRequest(String url , Map<String , Object> params , OkHttpHelper.HttpMethodType methodType)
    {
        Request.Builder builder = new Request.Builder().url(url);
        if (methodType == HttpMethodType.GET)
        {
            url = buildUrlParams(url , params);
            builder.url(url);
            builder.get();
        }
        else if(methodType == HttpMethodType.POST)
        {
            RequestBody body = buildFormData(params);
            builder.post(body);
        }
        return builder.build();
    }

    private String buildUrlParams(String url , Map<String , Object> params)
    {
        if (params == null)
        {
            params = new HashMap<>(1);
        }
        String token = StoreApplication.getInstance().getToken();
        if (!TextUtils.isEmpty(token))
            params.put("token" , token);
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String , Object> entry : params.entrySet())
        {
            sb.append(entry.getKey() + "=" + (entry.getValue() == null ? "" : entry.getValue().toString()));
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&"))
        {
            s = s.substring(0 , s.length() - 1);
        }
        if (url.indexOf("?") > 0)
        {
            url = url + "&" + s;
        }
        else
        {
            url = url + "?" + s;
        }
        return url;
    }

    private RequestBody buildFormData(Map<String , Object> params)
    {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        if (params != null)
        {
            for (Map.Entry<String , Object> entry : params.entrySet())
            {
                builder.add(entry.getKey() , entry.getValue() == null ? "" : entry.getValue().toString());
            }
            String token = StoreApplication.getInstance().getToken();
            if (!TextUtils.isEmpty(token))
            {
                builder.add("token" , token);
            }
        }
        return builder.build();
    }

    enum HttpMethodType
    {
        GET,POST
    }
}
