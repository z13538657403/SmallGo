package com.test.zhangtao.activitytest.http;

import com.google.gson.internal.$Gson$Types;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by zhangtao on 16/9/29.
 */
public abstract class BaseCallBack<T>
{
    public Type mType;

    static Type getSuperclassTypeParameter(Class<?> subClass)
    {
        Type superclass = subClass.getGenericSuperclass();
        if (superclass instanceof Class)
        {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    public BaseCallBack()
    {
        mType = getSuperclassTypeParameter(getClass());
    }

    public abstract void onRequestBefore(Request request);

    public abstract void onFailure(Request request , IOException e);

    /**
     *  请求成功时调用此方法
     */
    public abstract void onResponse(Response response);

    /**
     * 状态码大于200，小于300时调用此方法
     */
    public abstract void onSuccess(Response response , T t);

    /**
     * 状态码400，403，500等时调用此方法
     */
    public abstract void onError(Response response , int code , Exception e);

    /**
     * Token验证失败，状态码401，402，403等时调用此方法
     */
    public abstract void onTokenError(Response response , int code);
}
