package com.test.zhangtao.activitytest.pagers;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.okhttp.Response;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.bean.Address;
import com.test.zhangtao.activitytest.city.CityModel;
import com.test.zhangtao.activitytest.city.DistrictModel;
import com.test.zhangtao.activitytest.city.ProvinceModel;
import com.test.zhangtao.activitytest.city.XmlParserHandler;
import com.test.zhangtao.activitytest.http.DialogCallBack;
import com.test.zhangtao.activitytest.http.OkHttpHelper;
import com.test.zhangtao.activitytest.http.SimpleCallBack;
import com.test.zhangtao.activitytest.msg.BaseRespMsg;
import com.test.zhangtao.activitytest.widget.ClearEditText;
import com.test.zhangtao.activitytest.widget.CnToolBar;
import com.test.zhangtao.activitytest.widget.Contants;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by zhangtao on 16/11/5.
 */
public class AddressAddActivity extends BaseActivity
{
    private OptionsPickerView mCityPickerView;

    @ViewInject(R.id.txt_address)
    private TextView mTxtAddress;

    @ViewInject(R.id.editTxt_consignee)
    private ClearEditText mEditConsignee;

    @ViewInject(R.id.editTxt_myPhone)
    private ClearEditText mEditPhone;

    @ViewInject(R.id.editTxt_add)
    private ClearEditText mEditAddr;

    @ViewInject(R.id.activity_address_add_toolbar)
    private CnToolBar mToolBar;

    private ArrayList<ProvinceModel> mProvinces = new ArrayList<>();
    private ArrayList<ArrayList<CityModel>> mCities = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<DistrictModel>>> mDistricts = new ArrayList<>();
    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    private Address updateAddress;
    private int addressStatus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_address_add);
        ViewUtils.inject(this);

        updateAddress = (Address) getIntent().getSerializableExtra("Address");
        addressStatus = getIntent().getIntExtra("NewOrUpdate" , -1);

        initToolbar();
        init();
        showUpdateData();
    }

    private void showUpdateData()
    {
        switch (addressStatus)
        {
            case 0:
                mToolBar.setTitle(getString(R.string.new_address));
                break;
            case 1:
                mToolBar.setTitle(getString(R.string.update_address));
                if (updateAddress != null)
                {
                    mEditConsignee.setText(updateAddress.getConsignee());
                    mEditPhone.setText(updateAddress.getPhone());
                    String[] addressStr = updateAddress.getAddr().split("-");
                    Log.d("UpdateAddress" , updateAddress.getAddr() + "--->" + addressStr[0] + "-----" + addressStr[1]);
                    mTxtAddress.setText(addressStr[0]);
                    mEditAddr.setText(addressStr[1]);
                }
                break;
            default:
                break;
        }
    }

    private void initToolbar()
    {
        mToolBar.setRightButtonOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                switch (addressStatus)
                {
                    case 0:
                        createAddress();
                        break;
                    case 1:
                        modifyAddress();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void modifyAddress()
    {
        if (updateAddress != null)
        {
            Map<String , Object> params = new HashMap<>(8);
            String address = mTxtAddress.getText().toString() + "-" + mEditAddr.getText().toString();
            params.put("id" , updateAddress.getId());
            params.put("consignee" , mEditConsignee.getText().toString());
            params.put("phone" , mEditPhone.getText().toString());
            params.put("addr" , address);
            params.put("zip_code" , updateAddress.getZipCode());
            params.put("is_default" , updateAddress.getDefault());

            okHttpHelper.post(Contants.ADDRESS_UPDATE, params, new SimpleCallBack<BaseRespMsg>(this)
            {
                @Override
                public void onSuccess(Response response, BaseRespMsg baseRespMsg)
                {
                    if (baseRespMsg.getStatus() == BaseRespMsg.STATUS_SUCCESS)
                    {
                        setResult(RESULT_OK);
                        finish();
                    }
                }

                @Override
                public void onError(Response response, int code, Exception e)
                {
                }
            });
        }
    }

    private void createAddress()
    {
        String consignee = mEditConsignee.getText().toString();
        String phone = mEditPhone.getText().toString();
        String address = mTxtAddress.getText().toString() + "-" + mEditAddr.getText().toString();
        Map<String , Object> params = new HashMap<>(6);
        params.put("user_id" , StoreApplication.getInstance().getUser().getId());
        params.put("consignee" , consignee);
        params.put("phone" , phone);
        params.put("addr" , address);
        params.put("zip_code" , "000000");

        okHttpHelper.post(Contants.ADDRESS_CREATE, params, new DialogCallBack<BaseRespMsg>(this)
        {
            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg)
            {
                if (baseRespMsg.getStatus() == BaseRespMsg.STATUS_SUCCESS)
                {
                    setResult(RESULT_OK);
                    finish();
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    private void init()
    {
        initProvinceDatas();
        try
        {
            //选项选择器
            mCityPickerView = new OptionsPickerView(this);
            mCityPickerView.setPicker( mProvinces, mCities, mDistricts, true);
            mCityPickerView.setTitle("选择城市");
            mCityPickerView.setCyclic(false, false , false);
            mCityPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int option2, int options3)
                {
                    String address = mProvinces.get(options1).getName() + "  "
                            + mCities.get(options1).get(option2) + "  "
                            + mDistricts.get(options1).get(option2).get(options3);
                    mTxtAddress.setText(address);
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.ll_city_picker)
    public void showpickerView(View view)
    {
        mCityPickerView.show();
    }

    protected void initProvinceDatas()
    {
        AssetManager asset = getAssets();
        try
        {
            InputStream input = asset.open("province_data.xml");
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input , handler);
            input.close();
            //获取解析出来的数据
            mProvinces = (ArrayList<ProvinceModel>) handler.getDataList();
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }

        if (mProvinces != null)
        {
            for (ProvinceModel p : mProvinces)
            {
                List<CityModel> cities = p.getCityList();
                //组装城市数据
                mCities.add((ArrayList<CityModel>) cities);
                ArrayList<ArrayList<DistrictModel>> dts = new ArrayList<>();
                for (CityModel c : cities)
                {
                    List<DistrictModel> districts = c.getDistrictList();
                    dts.add((ArrayList<DistrictModel>) districts);
                }
                mDistricts.add(dts);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            Toast.makeText(this , "为防止数据丢失，请点击保存" , Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
