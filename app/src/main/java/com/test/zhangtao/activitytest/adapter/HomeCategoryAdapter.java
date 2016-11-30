package com.test.zhangtao.activitytest.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.picasso.Picasso;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.bean.Campaign;
import com.test.zhangtao.activitytest.bean.HomeCampaign;
import com.test.zhangtao.activitytest.bean.HomeCategory;

import java.util.List;

/**
 * Created by zhangtao on 16/9/26.
 */
public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.ViewHolder>
{
    private static int VIEW_TYPE_L = 0;
    private static int VIEW_TYPE_R = 1;
    private LayoutInflater inflater;
    private List<HomeCampaign> mDatas;
    private Context mContext;
    private OnCampaignClickListener mListener;

    public void setOnCampaignClickListener(OnCampaignClickListener listener)
    {
        this.mListener = listener;
    }

    public HomeCategoryAdapter(List<HomeCampaign> datas , Context context)
    {
        this.mDatas = datas;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int type)
    {
        inflater = LayoutInflater.from(parent.getContext());
        if (type == VIEW_TYPE_R)
        {
            return new ViewHolder(inflater.inflate(R.layout.home_cardview2 , parent , false));
        }

        return new ViewHolder(inflater.inflate(R.layout.home_cardview , parent , false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        HomeCampaign homeCampaign = mDatas.get(position);
        holder.textTitle.setText(homeCampaign.getTitle());

        Picasso.with(mContext).load(homeCampaign.getCpOne().getImgUrl()).into(holder.imageViewBig);
        Picasso.with(mContext).load(homeCampaign.getCpTwo().getImgUrl()).into(holder.imageViewSmallTop);
        Picasso.with(mContext).load(homeCampaign.getCpThree().getImgUrl()).into(holder.imageViewSmallBottom);
    }

    @Override
    public int getItemCount()
    {
        return mDatas.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        if (position % 2 == 0)
        {
            return VIEW_TYPE_R;
        }
        else
        {
            return VIEW_TYPE_L;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView textTitle;
        ImageView imageViewBig;
        ImageView imageViewSmallTop;
        ImageView imageViewSmallBottom;

        public ViewHolder(View itemView)
        {
            super(itemView);

            textTitle = (TextView) itemView.findViewById(R.id.text_title);
            imageViewBig = (ImageView) itemView.findViewById(R.id.image_big);
            imageViewSmallTop = (ImageView) itemView.findViewById(R.id.image_small_top);
            imageViewSmallBottom = (ImageView) itemView.findViewById(R.id.image_small_bottom);

            imageViewBig.setOnClickListener(this);
            imageViewSmallTop.setOnClickListener(this);
            imageViewSmallBottom.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            if (mListener != null)
            {
                anim(view);
            }
        }

        private void anim(final View v)
        {
            ObjectAnimator animator = ObjectAnimator.ofFloat(v , "rotationX" , 0.0f , 360.0f).setDuration(300);
            animator.addListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationEnd(Animator animation)
                {
                    HomeCampaign home = mDatas.get(getLayoutPosition());
                    switch (v.getId())
                    {
                        case R.id.image_big:
                            mListener.onClick(v , home.getCpTwo());
                            break;
                        case R.id.image_small_top:
                            mListener.onClick(v , home.getCpTwo());
                            break;
                        case R.id.image_small_bottom:
                            mListener.onClick(v , home.getCpThree());
                            break;
                    }
                }
            });
            animator.start();
        }
    }

    public interface OnCampaignClickListener
    {
        void onClick(View view , Campaign campaign);
    }
}
