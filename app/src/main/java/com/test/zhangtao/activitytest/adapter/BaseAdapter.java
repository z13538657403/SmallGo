package com.test.zhangtao.activitytest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

/**
 * Created by zhangtao on 16/10/1.
 */
public abstract class BaseAdapter<T , H extends BaseViewHolder> extends RecyclerView.Adapter<BaseViewHolder>
{
    protected List<T> datas;
    protected LayoutInflater mInflater;
    protected Context mContext;
    protected int mLayoutResId;
    protected OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.listener = listener;
    }

    public interface OnItemClickListener
    {
        void onClick(View view , int position);
    }

    public BaseAdapter(Context context, List<T> datas , int layoutResId)
    {
        this.mContext = context;
        this.datas = datas;
        mInflater = LayoutInflater.from(context);
        this.mLayoutResId = layoutResId;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = mInflater.inflate(mLayoutResId , null , false);
        return new BaseViewHolder(view , listener);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position)
    {
        T t = getItem(position);
        bindData(holder , t);
    }

    @Override
    public int getItemCount()
    {
        if (datas == null || datas.size() <= 0)
        return 0;
        return datas.size();
    }

    public T getItem(int position)
    {
        return datas.get(position);
    }

    public void clearData()
    {
        if (datas == null || datas.size() <= 0)
            return;
        datas.clear();
        notifyItemRangeRemoved(0 , datas.size());
    }

    public void addData(List<T> data)
    {
        addData(0 , data);
    }

    public List<T> getData()
    {
        return datas;
    }

    public void addData(int position , List<T> data)
    {
        if (data != null && data.size() > 0)
        {
            datas.addAll(data);
            notifyItemRangeChanged(position , datas.size());
        }
    }

    public abstract void bindData(BaseViewHolder viewHolder , T t);
}
