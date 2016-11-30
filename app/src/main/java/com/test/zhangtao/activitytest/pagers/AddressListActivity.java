package com.test.zhangtao.activitytest.pagers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Response;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.adapter.AddressAdapter;
import com.test.zhangtao.activitytest.bean.Address;
import com.test.zhangtao.activitytest.decoration.DividerItemDecoration1;
import com.test.zhangtao.activitytest.http.OkHttpHelper;
import com.test.zhangtao.activitytest.http.SimpleCallBack;
import com.test.zhangtao.activitytest.msg.BaseRespMsg;
import com.test.zhangtao.activitytest.widget.CnToolBar;
import com.test.zhangtao.activitytest.widget.Contants;
import com.test.zhangtao.activitytest.widget.WrapContentLinearLayoutManager;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangtao on 16/11/5.
 */
public class AddressListActivity extends BaseActivity
{
    private static final int NEW_ADDRESS = 0;
    private static final int UPDATE_ADDRESS = 1;

    @ViewInject(R.id.address_list_toolbar)
    private CnToolBar mToolBar;

    @ViewInject(R.id.address_recycler_view)
    private RecyclerView mRecyclerview;

    private OkHttpHelper mHttpHelper = OkHttpHelper.getInstance();
    private AddressAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_address_list);
        ViewUtils.inject(this);

        initToolBar();
        initAddress();
    }

    private void initToolBar()
    {
        mToolBar.setRightButtonOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AddressListActivity.this , AddressAddActivity.class);
                intent.putExtra("NewOrUpdate" , NEW_ADDRESS);
                startActivityForResult(intent , Contants.REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        initAddress();
    }

    private void initAddress()
    {
        Map<String , Object> params = new HashMap<>(1);
        params.put("user_id" , StoreApplication.getInstance().getUser().getId());

        mHttpHelper.get(Contants.ADDRESS_LIST, params, new SimpleCallBack<List<Address>>(this)
        {
            @Override
            public void onSuccess(Response response, List<Address> addresses)
            {
                showAdapter(addresses);
            }

            @Override
            public void onError(Response response, int code, Exception e)
            {
            }
        });
    }

    private void showAdapter(List<Address> addresses)
    {
        Collections.sort(addresses);
        if (mAdapter == null)
        {
            mAdapter = new AddressAdapter(this, addresses, new AddressAdapter.AddressListener()
            {
                @Override
                public void setDefault(Address address)
                {
                    updateDefaultAddress(address);
                }

                @Override
                public void delAddress(long address_id)
                {
                    showDelDialog(address_id);
                }

                @Override
                public void updateAddress(Address address)
                {
                    toUpdateActivity(address);
                }
            });
            mRecyclerview.setAdapter(mAdapter);
            mRecyclerview.setLayoutManager(new WrapContentLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            mRecyclerview.addItemDecoration(new DividerItemDecoration1(this, DividerItemDecoration1.VERTICAL_LIST));
        }
        else
        {
            mAdapter.clearData();
            mAdapter.addData(addresses);
            mRecyclerview.scrollToPosition(0);
        }
    }

    private void toUpdateActivity(Address address)
    {
        Intent intent = new Intent(AddressListActivity.this , AddressAddActivity.class);
        intent.putExtra("Address" , address);
        intent.putExtra("NewOrUpdate" , UPDATE_ADDRESS);
        startActivityForResult(intent , Contants.REQUEST_CODE);
    }

    private void showDelDialog(final long address_id)
    {
        new AlertDialog.Builder(this)
                .setTitle("删除地址")
                .setMessage("您确定删除该地址？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        deleteAddress(address_id);
                    }
                }).setNegativeButton("取消" , null).show();
    }

    private void deleteAddress(long address_id)
    {
        Map<String , Object> params = new HashMap<>(1);
        params.put("id" , address_id + "");

        mHttpHelper.post(Contants.ADDRESS_DEL, params, new SimpleCallBack<BaseRespMsg>(this)
        {
            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg)
            {
                if (baseRespMsg.getStatus() == BaseRespMsg.STATUS_SUCCESS)
                {
                    initAddress();
                }
            }
            @Override
            public void onError(Response response, int code, Exception e)
            {
                Log.d("Error" , code + "");
            }
        });
    }

    public void updateDefaultAddress(Address address)
    {
        Map<String , Object> params = new HashMap<>(8);
        params.put("id" , address.getId());
        params.put("consignee" , address.getConsignee());
        params.put("phone" , address.getPhone());
        params.put("addr" , address.getAddr());
        params.put("zip_code" , address.getZipCode());
        params.put("is_default" , address.getDefault());

        mHttpHelper.post(Contants.ADDRESS_UPDATE, params, new SimpleCallBack<BaseRespMsg>(this)
        {
            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg)
            {
                if (baseRespMsg.getStatus() == BaseRespMsg.STATUS_SUCCESS)
                    initAddress();
            }

            @Override
            public void onError(Response response, int code, Exception e)
            {
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            setResult(RESULT_OK);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
