package com.test.zhangtao.activitytest.adapter;

import android.content.Context;
import android.widget.*;

import java.util.List;

/**
 * Created by zhangtao on 16/10/1.
 */
public abstract class SimpleAdapter<T> extends BaseAdapter<T , BaseViewHolder>
{
    public SimpleAdapter(Context context, List<T> datas, int layoutResId)
    {
        super(context, datas, layoutResId);
    }
}
