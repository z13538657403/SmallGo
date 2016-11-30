package com.test.zhangtao.activitytest.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.Button;

import com.facebook.drawee.view.SimpleDraweeView;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.bean.Favorites;
import com.test.zhangtao.activitytest.bean.ShoppingCar;
import com.test.zhangtao.activitytest.bean.Wares;

import java.util.List;

/**
 * Created by zhangtao on 16/11/7.
 */
public class FavoriteAdapter extends SimpleAdapter<Favorites>
{
    private FavoriteListener listener;
    public FavoriteAdapter(Context context, List<Favorites> datas , FavoriteListener listener)
    {
        super(context, datas, R.layout.template_my_favorite);
        this.listener = listener;
    }

    @Override
    public void bindData(BaseViewHolder viewHolder, final Favorites favorites)
    {
        final Wares wares = favorites.getWares();
        SimpleDraweeView draweeView = (SimpleDraweeView) viewHolder.getView(R.id.favorite_draweeView);
        draweeView.setImageURI(Uri.parse(wares.getImgUrl()));
        viewHolder.getTextView(R.id.favorite_product_title).setText(wares.getName());
        viewHolder.getTextView(R.id.favorite_product_price).setText("¥ " + wares.getPrice());

        Button buttonRemove = viewHolder.getButton(R.id.btn_delete_favorite);
        Button buttonToCart = viewHolder.getButton(R.id.btn_put_cart);
        buttonRemove.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (listener != null)
                {
                    listener.removeFavorite(favorites.getId());
                }
            }
        });

        buttonToCart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (listener != null)
                {
                    listener.addToFavorite(convertData(wares));
                }
            }
        });
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

    public interface FavoriteListener
    {
        void removeFavorite(long favorite_id);
        void addToFavorite(ShoppingCar shoppingCar);
    }
}
