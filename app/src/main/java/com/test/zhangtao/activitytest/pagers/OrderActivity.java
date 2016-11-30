package com.test.zhangtao.activitytest.pagers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.pingplusplus.android.Pingpp;
import com.squareup.okhttp.Response;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.adapter.WareOrderAdapter;
import com.test.zhangtao.activitytest.bean.Address;
import com.test.zhangtao.activitytest.bean.Charge;
import com.test.zhangtao.activitytest.bean.ShoppingCar;
import com.test.zhangtao.activitytest.http.DialogCallBack;
import com.test.zhangtao.activitytest.http.OkHttpHelper;
import com.test.zhangtao.activitytest.http.SimpleCallBack;
import com.test.zhangtao.activitytest.msg.BaseRespMsg;
import com.test.zhangtao.activitytest.msg.CreateOrderRespMsg;
import com.test.zhangtao.activitytest.util.CarProvider;
import com.test.zhangtao.activitytest.util.JSONUtil;
import com.test.zhangtao.activitytest.widget.CnToolBar;
import com.test.zhangtao.activitytest.widget.Contants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangtao on 16/11/2.
 */
public class OrderActivity extends BaseActivity implements View.OnClickListener
{
    /**
     * 银联支付渠道
     */
    private static final String CHANNEL_UPACP = "upacp";
    /**
     * 微信支付渠道
     */
    private static final String CHANNEL_WECHAT = "wx";
    /**
     * 微信支付渠道
     */
    private static final String CHANNEL_QPAY = "qpay";
    /**
     * 支付支付渠道
     */
    private static final String CHANNEL_ALIPAY = "alipay";
    /**
     * 百度支付渠道
     */
    private static final String CHANNEL_BFB = "bfb";
    /**
     * 京东支付渠道
     */
    private static final String CHANNEL_JDPAY_WAP = "jdpay_wap";

    @ViewInject(R.id.txt_order)
    private TextView txtOrder;

    @ViewInject(R.id.order_toolbar)
    private CnToolBar mToolBar;

    @ViewInject(R.id.name_phone)
    private TextView orderNameAndPhone;

    @ViewInject(R.id.order_address)
    private TextView orderAddress;

    @ViewInject(R.id.order_recycler_view)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.rl_alipay)
    private RelativeLayout mLayoutAlipay;

    @ViewInject(R.id.rl_wechat)
    private RelativeLayout mLayoutWechat;

    @ViewInject(R.id.rl_bd)
    private RelativeLayout mLayoutBd;

    @ViewInject(R.id.rb_alipay)
    private RadioButton mRbAlipay;

    @ViewInject(R.id.rb_wechat)
    private RadioButton mRbWechat;

    @ViewInject(R.id.rb_bd)
    private RadioButton mRbBd;

    @ViewInject(R.id.txt_total)
    private TextView mTxtTotal;

    @ViewInject(R.id.btn_createOrder)
    private Button mBtnCreateOrder;

    private CarProvider carProvider;
    private WareOrderAdapter mAdapter;
    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();
    private Address myDefaultAddress;

    private String orderNum;
    private String currentPayChannel = CHANNEL_ALIPAY;
    private float amount;

    private HashMap<String , RadioButton> channels = new HashMap<>(3);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_create_order);
        ViewUtils.inject(this);

        initToolBar();
        showData();
        initAddress();
        init();
    }

    private void initToolBar()
    {
        mToolBar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                OrderActivity.this.finish();
            }
        });
    }

    private void showData()
    {
        carProvider = CarProvider.getInstance();
        carProvider.setMyConext(this);
        mAdapter = new WareOrderAdapter(this , carProvider.getAll());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this , LinearLayoutManager.HORIZONTAL , false));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initAddress()
    {
        Map<String , Object> params = new HashMap<>(1);
        params.put("user_id" , StoreApplication.getInstance().getUser().getId());

        okHttpHelper.get(Contants.ADDRESS_LIST , params, new SimpleCallBack<List<Address>>(this)
        {
            @Override
            public void onSuccess(Response response, List<Address> addresses)
            {
                getDefaultAddress(addresses);
            }

            @Override
            public void onError(Response response, int code, Exception e)
            {
            }
        });
    }

    private void getDefaultAddress(List<Address> addresses)
    {
        if (addresses != null)
        {
            for (Address address : addresses)
            {
                if (address.getDefault())
                    myDefaultAddress = address;
            }
        }

        if (myDefaultAddress != null)
        {
            String nameAndPhone = myDefaultAddress.getConsignee() + "(" + myDefaultAddress.getPhone() + ")";
            orderNameAndPhone.setText(nameAndPhone);
            orderAddress.setText(myDefaultAddress.getAddr());
        }
    }

    private void init()
    {
        channels.put(CHANNEL_ALIPAY , mRbAlipay);
        channels.put(CHANNEL_WECHAT , mRbWechat);
        channels.put(CHANNEL_BFB , mRbBd);

        mLayoutAlipay.setOnClickListener(this);
        mLayoutWechat.setOnClickListener(this);
        mLayoutBd.setOnClickListener(this);

        amount = mAdapter.getTotalPrice();
        mTxtTotal.setText("应付款：  ¥" + amount);
    }

    @Override
    public void onClick(View view)
    {
        selectPayChannel(view.getTag().toString());
    }

    private void selectPayChannel(String payChannel)
    {
        for (Map.Entry<String , RadioButton> channel : channels.entrySet())
        {
            currentPayChannel = payChannel;
            RadioButton rb = channel.getValue();
            if (channel.getKey().equals(payChannel))
            {
                boolean isCheck = rb.isChecked();
                rb.setChecked(!isCheck);
            }
            else
            {
                rb.setChecked(false);
            }
        }
    }

    @OnClick(R.id.btn_createOrder)
    public void createNewOrder(View view)
    {
        if (orderAddress.getText().toString().trim().equals(""))
        {
            Toast.makeText(this , "请先选择收获地址" , Toast.LENGTH_SHORT).show();
            return;
        }
        postNewOrder();
        carProvider.clear();
    }

    @OnClick(R.id.rl_addr)
    public void changeAddress(View view)
    {
        Intent intent = new Intent(this , AddressListActivity.class);
        startActivityForResult(intent , Contants.REQUEST_CODE);
    }

    private void postNewOrder()
    {
        final List<ShoppingCar> carts = mAdapter.getData();
        List<WareItem> items = new ArrayList<>(carts.size());
        for (ShoppingCar c : carts)
        {
            WareItem item = new WareItem(c.getId() , c.getPrice().intValue());
            items.add(item);
        }

        String item_json = JSONUtil.toJSON(items);

        Map<String , Object> params = new HashMap<>(5);
        params.put("user_id" , StoreApplication.getInstance().getUser().getId() + "");
        params.put("item_json" , item_json);
        params.put("pay_channel" , currentPayChannel);
        params.put("amount" , (int)amount + "");
        params.put("addr_id" , myDefaultAddress.getId() + "");

        mBtnCreateOrder.setEnabled(false);

        okHttpHelper.post(Contants.ORDER_CREATE, params, new DialogCallBack<CreateOrderRespMsg>(this) {
            @Override
            public void onSuccess(Response response, CreateOrderRespMsg respMsg)
            {
                mBtnCreateOrder.setEnabled(true);
                orderNum = respMsg.getData().getOrderNum();
                Charge charge = respMsg.getData().getCharge();
                openPaymentActivity(JSONUtil.toJSON(charge));
            }

            @Override
            public void onError(Response response, int code, Exception e)
            {
                mBtnCreateOrder.setEnabled(true);
            }
        });
    }

    private void openPaymentActivity(String charge)
    {
        Pingpp.createPayment(OrderActivity.this , charge);
    }

    @Override
    protected void onActivityResult(int requestCode , int resultCode , Intent data)
    {
        //地址选择返回处理
        if (requestCode == Contants.REQUEST_CODE)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                initAddress();
            }
        }

        //支付返回处理
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                String result = data.getExtras().getString("pay_result");
                if (result.equals("success"))
                {
                    changeOrderStatus(1);
                }
                else if(result.equals("fail"))
                {
                    changeOrderStatus(-1);
                }
                else if (result.equals("cancel"))
                {
                    changeOrderStatus(-2);
                }
                else
                {
                    changeOrderStatus(0);
                }
            }
        }
    }

    private void changeOrderStatus(final int status)
    {
        Map<String , Object> params = new HashMap<>(5);
        params.put("order_num" , orderNum);
        params.put("status" , status + "");

        okHttpHelper.post(Contants.ORDER_COMPLETE, params, new DialogCallBack<BaseRespMsg>(this)
        {
            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg)
            {
                toPayResultActivity(status);
            }

            @Override
            public void onError(Response response, int code, Exception e)
            {
                toPayResultActivity(-1);
            }
        });
    }

    private void toPayResultActivity(int status)
    {
        Intent intent = new Intent(this , PayResultActivity.class);
        intent.putExtra("status" , status);
        startActivity(intent);
        this.finish();
    }

    class WareItem
    {
        private long ware_id;
        private int amount;

        public WareItem(long ware_id, int amount)
        {
            this.ware_id = ware_id;
            this.amount = amount;
        }

        public long getWare_id()
        {
            return ware_id;
        }

        public void setWare_id(long ware_id)
        {
            this.ware_id = ware_id;
        }

        public int getAmount()
        {
            return amount;
        }

        public void setAmount(int amount)
        {
            this.amount = amount;
        }
    }
}