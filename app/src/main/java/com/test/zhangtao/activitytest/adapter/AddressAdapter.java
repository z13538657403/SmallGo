package com.test.zhangtao.activitytest.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.bean.Address;

import java.util.List;

/**
 * Created by zhangtao on 16/11/6.
 */
public class AddressAdapter extends SimpleAdapter<Address>
{
    private AddressListener listener;

    public AddressAdapter(Context context, List<Address> datas , AddressListener listener)
    {
        super(context, datas, R.layout.template_address);

        this.listener = listener;
    }

    @Override
    public void bindData(BaseViewHolder viewHolder, final Address item)
    {
        viewHolder.getTextView(R.id.txt_name).setText(item.getConsignee());
        viewHolder.getTextView(R.id.txt_phone).setText(replacePhoneNum(item.getPhone()));
        viewHolder.getTextView(R.id.txt_myAddress).setText(item.getAddr());

        final CheckBox checkBox = (CheckBox) viewHolder.getView(R.id.cb_is_default);
        final boolean isDefault = item.getDefault();
        checkBox.setChecked(isDefault);
        if (isDefault)
        {
            checkBox.setText("默认地址");
        }
        else
        {
            checkBox.setClickable(true);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
                {
                    if (isChecked && listener != null)
                    {
                        item.setDefault(true);
                        listener.setDefault(item);
                    }
                }
            });
        }

        TextView delTV = viewHolder.getTextView(R.id.txt_del);
        delTV.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (listener != null)
                {
                    listener.delAddress(item.getId());
                }
            }
        });

        TextView updateTV = viewHolder.getTextView(R.id.txt_edit);
        updateTV.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (listener != null)
                {
                    listener.updateAddress(item);
                }
            }
        });
    }

    public String replacePhoneNum(String phone)
    {
        return phone.substring(0 , phone.length() - (phone.substring(3)).length()) + "****" + phone.substring(7);
    }

    public interface AddressListener
    {
        void setDefault(Address address);

        void delAddress(long address_id);

        void updateAddress(Address address);
    }
}
