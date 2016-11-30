package com.test.zhangtao.activitytest.adapter;

import android.content.Context;

import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.bean.ProductCategory;

import java.util.List;

/**
 * Created by zhangtao on 16/10/1.
 */
public class CategoryAdapter extends SimpleAdapter<ProductCategory>
{
    public CategoryAdapter(Context context, List<ProductCategory> datas)
    {
        super(context, datas, R.layout.category_list_item);
    }

    @Override
    public void bindData(BaseViewHolder viewHolder, ProductCategory productCategory)
    {
        viewHolder.getTextView(R.id.category_title).setText(productCategory.getName());
    }
}
