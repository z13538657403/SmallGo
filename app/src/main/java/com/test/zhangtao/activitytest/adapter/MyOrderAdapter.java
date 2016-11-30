package com.test.zhangtao.activitytest.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.bean.Order;
import com.test.zhangtao.activitytest.bean.OrderItem;
import com.w4lle.library.NineGridAdapter;
import com.w4lle.library.NineGridlayout;
import java.util.List;

/**
 * Created by zhangtao on 16/11/6.
 */
public class MyOrderAdapter extends SimpleAdapter<Order>
{

    public MyOrderAdapter(Context context, List<Order> datas)
    {
        super(context, datas, R.layout.template_my_orders);
    }

    @Override
    public void bindData(BaseViewHolder viewHolder, Order item)
    {
        String orderNumStr = "订单号: " + item.getOrderNum();
        viewHolder.getTextView(R.id.txt_order_num).setText(orderNumStr);
        viewHolder.getTextView(R.id.txt_order_money).setText("实付金额： ¥: " + item.getAmount());

        TextView txtStatus = viewHolder.getTextView(R.id.txt_status);
        switch (item.getStatus())
        {
            case Order.STATUS_SUCCESS:
                txtStatus.setText("成功");
                txtStatus.setTextColor(Color.parseColor("#ff4CAF50"));
                break;

            case Order.STATUS_PAY_FALL:
                txtStatus.setText("支付失败");
                txtStatus.setTextColor(Color.parseColor("#ffF44336"));
                break;

            case Order.STATUS_PAY_WAIT:
                txtStatus.setText("等待支付");
                txtStatus.setTextColor(Color.parseColor("#ffFFEB3B"));
                break;
        }

        NineGridlayout nineGridlayout = (NineGridlayout) viewHolder.getView(R.id.iv_ngrid_layout);
        nineGridlayout.setGap(5);
        nineGridlayout.setDefaultWidth(45);
        nineGridlayout.setDefaultHeight(45);
        nineGridlayout.setAdapter(new OrderItemAdapter(mContext , item.getItems()));
    }

    class OrderItemAdapter extends NineGridAdapter
    {
        private List<OrderItem> items;
        public OrderItemAdapter(Context context , List<OrderItem> items)
        {
            super(context , items);
            this.items = items;
        }

        @Override
        public int getCount()
        {
            return (items == null) ? 0 : items.size();
        }

        @Override
        public String getUrl(int positopn)
        {
            OrderItem item = (OrderItem) getItem(positopn);
            return item.getWares().getImgUrl();
        }

        @Override
        public Object getItem(int position)
        {
            return (items == null) ? null : items.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            OrderItem item = (OrderItem) getItem(position);
            return (item == null) ? 0 : item.getId();
        }

        @Override
        public View getView(int i, View view)
        {
            ImageView iv = new ImageView(context);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv.setBackgroundColor(Color.parseColor("#f5f5f5"));
            Picasso.with(context).load(getUrl(i)).placeholder(new ColorDrawable(Color.parseColor("#f5f5f5"))).into(iv);
            return iv;
        }
    }
}
