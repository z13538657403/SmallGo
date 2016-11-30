package com.test.zhangtao.activitytest.adapter;

import android.content.Context;
import android.net.Uri;

import com.facebook.drawee.view.SimpleDraweeView;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.bean.Wares;

import java.util.List;

/**
 * Created by zhangtao on 16/10/1.
 */
public class WaresAdapter extends SimpleAdapter<Wares>
{
    public WaresAdapter(Context context, List<Wares> datas)
    {
        super(context, datas, R.layout.template_grid_item);
    }

    @Override
    public void bindData(BaseViewHolder viewHolder, Wares wares)
    {
        SimpleDraweeView draweeView = (SimpleDraweeView) viewHolder.getView(R.id.draw_view);
        draweeView.setImageURI(Uri.parse(wares.getImgUrl()));

        viewHolder.getTextView(R.id.wares_title).setText(wares.getName());
        viewHolder.getTextView(R.id.wares_price).setText("¥" + wares.getPrice() + "");
    }
}
