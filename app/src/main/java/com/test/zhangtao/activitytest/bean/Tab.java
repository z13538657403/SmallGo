package com.test.zhangtao.activitytest.bean;

/**
 * Created by zhangtao on 16/9/25.
 */
public class Tab
{
    private int icon;
    private int text;
    private Class fragment;

    public Tab(int icon, int text, Class fragment)
    {
        this.icon = icon;
        this.text = text;
        this.fragment = fragment;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public Class getFragment() {
        return fragment;
    }

    public void setFragment(Class fragment) {
        this.fragment = fragment;
    }

    public int getText() {
        return text;
    }

    public void setText(int text) {
        this.text = text;
    }
}
