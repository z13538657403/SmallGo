package com.test.zhangtao.activitytest.bean;

import java.io.Serializable;

/**
 * Created by zhangtao on 16/10/2.
 */
public class ShoppingCar extends Wares implements Serializable
{
    private int count;
    private boolean isChecked = true;

    public int getCount()
    {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
