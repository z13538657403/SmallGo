package com.test.zhangtao.activitytest.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.okhttp.Response;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.adapter.CarAdapter;
import com.test.zhangtao.activitytest.bean.ShoppingCar;
import com.test.zhangtao.activitytest.bean.User;
import com.test.zhangtao.activitytest.decoration.DividerItemDecoration1;
import com.test.zhangtao.activitytest.http.DialogCallBack;
import com.test.zhangtao.activitytest.http.OkHttpHelper;
import com.test.zhangtao.activitytest.pagers.OrderActivity;
import com.test.zhangtao.activitytest.util.CarProvider;
import com.test.zhangtao.activitytest.widget.CnToolBar;
import com.test.zhangtao.activitytest.widget.Contants;
import com.test.zhangtao.activitytest.widget.WrapContentLinearLayoutManager;

import java.util.List;

/**
 * Created by zhangtao on 16/9/25.
 */
public class ForthFragment extends BaseFragment implements View.OnClickListener
{
    public static final int ACTION_EDIT = 1;
    public static final int ACTION_COMPLETE = 2;

    @ViewInject(R.id.car_recyclerView)
    private RecyclerView carRecyclerView;
    @ViewInject(R.id.checkbox_all)
    private CheckBox allCheckBox;
    @ViewInject(R.id.txt_total)
    private TextView mTextTotal;
    @ViewInject(R.id.btn_order)
    private Button mBtnOrder;
    @ViewInject(R.id.btn_del)
    private Button mBtnDel;
    @ViewInject(R.id.toolbar)
    protected CnToolBar mToolbar;

    private CarAdapter mAdapter;
    private CarProvider carProvider;
    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance)
    {
        return inflater.inflate(R.layout.forth_fragment , container , false);
    }

    @Override
    public void init()
    {
        carProvider = CarProvider.getInstance();
        carProvider.setMyConext(getActivity());
        changeToolBar();
        showData();
    }

    private void changeToolBar()
    {
        mToolbar.getRightButton().setOnClickListener(this);
        mToolbar.getRightButton().setTag(ACTION_EDIT);
    }

    private void showData()
    {
        List<ShoppingCar> cars = carProvider.getAll();
        mAdapter = new CarAdapter(getContext() , cars , allCheckBox , mTextTotal);

        carRecyclerView.setAdapter(mAdapter);
        carRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity() ,
                LinearLayoutManager.VERTICAL , false));
        carRecyclerView.addItemDecoration(new DividerItemDecoration1(getContext() , DividerItemDecoration1.VERTICAL_LIST));
        carRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void refData()
    {
        mAdapter.clearData();
        List<ShoppingCar> cars = carProvider.getAll();
        mAdapter.addData(cars);
        mAdapter.showTotalPrice();
    }

    @Override
    public void onClick(View view)
    {
        int action = (int) view.getTag();
        if (ACTION_EDIT == action)
        {
            showDelControl();
        }
        else if (ACTION_COMPLETE == action)
        {
            hideDelControl();
        }
    }

    @OnClick(R.id.btn_del)
    public void delCar(View view)
    {
        mAdapter.delCar();
    }

    @OnClick(R.id.btn_order)
    public void toOrder(View view)
    {
//        okHttpHelper.get(Contants.USER_DETAIL, new DialogCallBack<User>(getActivity())
//        {
//            @Override
//            public void onSuccess(Response response, User user)
//            {
//                Log.d("OnSuccess" , response.code() + "");
//            }
//            @Override
//            public void onError(Response response, int code, Exception e)
//            {
//            }
//        });

        if (carProvider.getAll().size() == 0)
        {
            Toast.makeText(getActivity() , "购物车空空如也！！！" , Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(getActivity() , OrderActivity.class);
        startActivity(intent , true);
    }

    private void hideDelControl()
    {
        mToolbar.getRightButton().setText("编辑");
        mTextTotal.setVisibility(View.VISIBLE);
        mBtnOrder.setVisibility(View.VISIBLE);
        mBtnDel.setVisibility(View.GONE);
        mToolbar.getRightButton().setTag(ACTION_EDIT);

        mAdapter.checkAll_None(true);
        allCheckBox.setChecked(true);
    }

    private void showDelControl()
    {
        mToolbar.getRightButton().setText("完成");
        mTextTotal.setVisibility(View.GONE);
        mBtnOrder.setVisibility(View.GONE);
        mBtnDel.setVisibility(View.VISIBLE);
        mToolbar.getRightButton().setTag(ACTION_COMPLETE);

        mAdapter.checkAll_None(false);
        allCheckBox.setChecked(false);
    }
}
