package com.test.zhangtao.activitytest.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.facebook.drawee.view.SimpleDraweeView;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.bean.ShoppingCar;
import com.test.zhangtao.activitytest.bean.Wares;
import com.test.zhangtao.activitytest.util.CarProvider;

import java.util.List;

/**
 * Created by zhangtao on 16/10/1.
 */
public class HWAdapter extends SimpleAdapter<Wares>
{
    CarProvider provider;
    Context mContext;
    public HWAdapter(Context context, List<Wares> datas)
    {
        super(context, datas, R.layout.hot_product);
        provider = CarProvider.getInstance();
        provider.setMyConext(context);
        mContext = context;
    }

    @Override
    public void bindData(BaseViewHolder viewHolder, final Wares wares)
    {
        SimpleDraweeView mDrawView = (SimpleDraweeView) viewHolder.getView(R.id.drawee_view);
        mDrawView.setImageURI(Uri.parse(wares.getImgUrl()));

        viewHolder.getTextView(R.id.text_titles).setText(wares.getName());
        viewHolder.getTextView(R.id.text_price).setText(wares.getPrice() + "");
        Button button = viewHolder.getButton(R.id.button_add);
        if (button != null)
        {
            button.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    provider.put(convertData(wares));
                    Toast.makeText(mContext, "已经添加到购物车！", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public ShoppingCar convertData(Wares item)
    {
        ShoppingCar car = new ShoppingCar();
        car.setId(item.getId());
        car.setDescription(item.getDescription());
        car.setImgUrl(item.getImgUrl());
        car.setName(item.getName());
        car.setPrice(item.getPrice());

        return car;
    }

    public void resetLayout(int layoutId)
    {
        this.mLayoutResId = layoutId;
        notifyItemRangeChanged(0 , getData().size());
    }
}
