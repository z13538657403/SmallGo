package com.test.zhangtao.activitytest.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.picasso.Picasso;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.bean.Address;
import com.test.zhangtao.activitytest.bean.User;
import com.test.zhangtao.activitytest.pagers.AddressListActivity;
import com.test.zhangtao.activitytest.pagers.BaseActivity;
import com.test.zhangtao.activitytest.pagers.FavoriteActivity;
import com.test.zhangtao.activitytest.pagers.LoginActivity;
import com.test.zhangtao.activitytest.pagers.MyOrderActivity;
import com.test.zhangtao.activitytest.pagers.StoreApplication;
import com.test.zhangtao.activitytest.widget.Contants;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zhangtao on 16/9/25.
 */
public class FiveFragment extends BaseFragment
{
    @ViewInject(R.id.img_head)
    private CircleImageView mHeadImage;
    @ViewInject(R.id.txt_userName)
    private TextView userName;
    @ViewInject(R.id.btn_logout)
    private Button loginOut;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance)
    {
        return inflater.inflate(R.layout.five_fragment , container , false);
    }

    @Override
    public void init()
    {
        User user = StoreApplication.getInstance().getUser();
        showUser(user);
    }

    @OnClick(value = {R.id.img_head,R.id.txt_userName})
    public void toLogin(View view)
    {
        Intent intent = new Intent(getActivity() , LoginActivity.class);
        startActivityForResult(intent , Contants.REQUEST_CODE);
    }

    @OnClick(R.id.btn_logout)
    public void loginOut(View view)
    {
        StoreApplication.getInstance().clearUser();
        showUser(null);
    }

    @OnClick(R.id.txt_my_address)
    public void toAddress(View view)
    {
        toAnotherActivity(AddressListActivity.class);
    }

    @OnClick(R.id.txt_my_orders)
    public void toOrder(View view)
    {
        toAnotherActivity(MyOrderActivity.class);
    }

    @OnClick(R.id.txt_my_favorite)
    public void toMyFavorite(View view)
    {
        toAnotherActivity(FavoriteActivity.class);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        User user = StoreApplication.getInstance().getUser();
        showUser(user);
    }

    private void showUser(User user)
    {
        if (user != null)
        {
            userName.setText(user.getUsername());
            Picasso.with(getActivity()).load(user.getLogo_url()).into(mHeadImage);
            loginOut.setVisibility(View.VISIBLE);
            mHeadImage.setEnabled(false);
            userName.setEnabled(false);
        }
        else
        {
            userName.setText("请登录！！！");
            Picasso.with(getActivity()).load("http://192.168.1.101/default_head.png").into(mHeadImage);
            loginOut.setVisibility(View.INVISIBLE);
            mHeadImage.setEnabled(true);
            userName.setEnabled(true);
        }
    }

    private <T extends BaseActivity> void toAnotherActivity(Class<T> activity)
    {
        User user = StoreApplication.getInstance().getUser();
        if(user == null)
        {
            Toast.makeText(getActivity() , "请先登录！！！" , Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(getActivity() , activity);
        startActivity(intent);
    }
}
