package com.test.zhangtao.activitytest.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.bean.ShoppingCar;
import com.test.zhangtao.activitytest.util.CarProvider;
import com.test.zhangtao.activitytest.widget.NumberAddSubView;

import java.util.Iterator;
import java.util.List;

/**
 * Created by zhangtao on 16/10/2.
 */
public class CarAdapter extends SimpleAdapter<ShoppingCar> implements BaseAdapter.OnItemClickListener
{
    private CheckBox mCheckBox;
    private TextView textView;
    private CarProvider carProvider;

    public CarAdapter(Context context, List<ShoppingCar> datas , CheckBox checkBox , TextView textView)
    {
        super(context, datas, R.layout.template_car);
        this.mCheckBox = checkBox;
        this.textView = textView;
        carProvider = CarProvider.getInstance();
        carProvider.setMyConext(context);

        checkButtonListen();

        setOnItemClickListener(this);
        showTotalPrice();
    }

    @Override
    public void bindData(BaseViewHolder viewHolder, final ShoppingCar car)
    {
        viewHolder.getTextView(R.id.car_product_title).setText(car.getName());
        viewHolder.getTextView(R.id.car_product_price).setText("¥" + car.getPrice());
        SimpleDraweeView draweeView = (SimpleDraweeView) viewHolder.getView(R.id.car_product_img);
        draweeView.setImageURI(Uri.parse(car.getImgUrl()));

        CheckBox checkBox = (CheckBox) viewHolder.getView(R.id.checkbox_single);
        checkBox.setChecked(car.isChecked());

        NumberAddSubView numberAddSubView = (NumberAddSubView) viewHolder.getView(R.id.num_control);
        numberAddSubView.setValue(car.getCount());
        numberAddSubView.setOnButtonClickListener(new NumberAddSubView.OnButtonClickListener()
        {
            @Override
            public void onButtonAddClick(View view, int value)
            {
                car.setCount(value);
                carProvider.update(car);
                showTotalPrice();
            }
            @Override
            public void onButtonSubCLick(View view, int value)
            {
                car.setCount(value);
                carProvider.update(car);
                showTotalPrice();
            }
        });
    }

    @Override
    public void onClick(View view, int position)
    {
        ShoppingCar car = getItem(position);
        car.setChecked(!car.isChecked());
        notifyItemChanged(position);
        checkListen();
        showTotalPrice();
    }

    private void checkButtonListen()
    {
        mCheckBox.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                checkAll_None(mCheckBox.isChecked());
                showTotalPrice();
            }
        });
    }

    public void checkAll_None(boolean checked)
    {
        if (!isNull())
            return;
        int i = 0;
        for (ShoppingCar car : datas)
        {
            car.setChecked(checked);
            notifyItemChanged(i);
            i++;
        }
    }

    private void checkListen()
    {
        int count = 0;
        int checkNum = 0;
        if (datas != null)
        {
            count = datas.size();

            for (ShoppingCar car : datas)
            {
                if (!car.isChecked())
                {
                    mCheckBox.setChecked(false);
                    break;
                }
                else
                {
                    checkNum = checkNum + 1;
                }
            }
            if (count == checkNum)
            {
                mCheckBox.setChecked(true);
            }
        }
    }


    public void showTotalPrice()
    {
        float total = getTotalPrice();
        textView.setText(Html.fromHtml("合计 ¥<span style='color:#eb4f38'>" + total + "</span>") , TextView.BufferType.SPANNABLE);
    }

    private float getTotalPrice()
    {
        float sum = 0;
        if (!isNull())
        {
            return sum;
        }
        for (ShoppingCar car : datas)
        {
            if (car.isChecked())
            {
                sum += car.getCount() * car.getPrice();
            }
        }
        return sum;
    }

    public void delCar()
    {
        if (!isNull())
            return;

        for (Iterator iterator = datas.iterator();iterator.hasNext();)
        {
            ShoppingCar car = (ShoppingCar) iterator.next();
            if (car.isChecked())
            {
                int position = datas.indexOf(car);
                carProvider.delete(car);
                iterator.remove();
                notifyItemRemoved(position);
            }
        }
        showTotalPrice();
    }

    private boolean isNull()
    {
        return (datas != null && datas.size() > 0);
    }
}
