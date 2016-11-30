package com.test.zhangtao.activitytest.pagers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.bean.Tab;
import com.test.zhangtao.activitytest.fragments.FirstFragment;
import com.test.zhangtao.activitytest.fragments.FiveFragment;
import com.test.zhangtao.activitytest.fragments.ForthFragment;
import com.test.zhangtao.activitytest.fragments.SecondFragment;
import com.test.zhangtao.activitytest.fragments.ThirdFragment;
import com.test.zhangtao.activitytest.widget.FragmentTabHost;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangtao on 16/9/25.
 */
public class MainActivity extends BaseActivity
{
    private LayoutInflater inflater;
    private List<Tab> tabs;
    private ForthFragment forthFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_activity);

        initData();
    }

    private void initData()
    {
        tabs = new ArrayList<>();
        final Tab tab1 = new Tab(R.drawable.selector_icon_home , R.string.tab01 , FirstFragment.class);
        Tab tab2 = new Tab(R.drawable.selector_icon_hot , R.string.tab02 , SecondFragment.class);
        Tab tab3 = new Tab(R.drawable.selector_icon_dicover , R.string.tab03 , ThirdFragment.class);
        Tab tab4 = new Tab(R.drawable.selector_icon_cart , R.string.tab04 , ForthFragment.class);
        Tab tab5 = new Tab(R.drawable.selector_icon_user , R.string.tab05 , FiveFragment.class);

        tabs.add(tab1);
        tabs.add(tab2);
        tabs.add(tab3);
        tabs.add(tab4);
        tabs.add(tab5);

        inflater = LayoutInflater.from(this);
        FragmentTabHost fragmentTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        fragmentTabHost.setup(this , getSupportFragmentManager() , R.id.realContent);
        fragmentTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);

        for (Tab tab : tabs)
        {
            FragmentTabHost.TabSpec tabSpec = fragmentTabHost.newTabSpec(getString(tab.getText()));
            View view = inflater.inflate(R.layout.tab , null);
            ImageView imageView = (ImageView) view.findViewById(R.id.tab_image);
            imageView.setBackgroundResource(tab.getIcon());
            TextView textView = (TextView) view.findViewById(R.id.tab_text);
            textView.setText(tab.getText());

            tabSpec.setIndicator(view);
            fragmentTabHost.addTab(tabSpec , tab.getFragment() , null);
        }

        fragmentTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener()
        {
            @Override
            public void onTabChanged(String tabId)
            {
                if (tabId == getString(R.string.tab04))
                {
                    refreshCar();
                }
            }
        });
    }

    private void refreshCar()
    {
        if (forthFragment == null)
        {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.tab04));
            if (fragment != null)
            {
                forthFragment = (ForthFragment) fragment;
                forthFragment.refData();
            }
        }
        else
        {
            forthFragment.refData();
        }
    }
}
