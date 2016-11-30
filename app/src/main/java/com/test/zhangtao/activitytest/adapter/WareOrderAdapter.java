package com.test.zhangtao.activitytest.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.facebook.drawee.view.SimpleDraweeView;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.bean.ShoppingCar;

import java.util.List;

/**
 * Created by zhangtao on 16/11/3.
 */
public class WareOrderAdapter extends SimpleAdapter<ShoppingCar>
{
    public WareOrderAdapter(Context context, List<ShoppingCar> datas)
    {
        super(context, datas, R.layout.template_order_wares);
    }

    @Override
    public void bindData(BaseViewHolder viewHolder, ShoppingCar item)
    {
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) viewHolder.getView(R.id.order_drawee_view);
        simpleDraweeView.setImageURI(item.getImgUrl());
    }

    public float getTotalPrice()
    {
        float sum = 0;
        if (!isNull())
            return sum;

        for (ShoppingCar cart : datas)
        {
            sum += cart.getCount() * cart.getPrice();
        }

        return sum;
    }

    private boolean isNull()
    {
        return (datas != null && datas.size() > 0);
    }
}
