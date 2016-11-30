package com.test.zhangtao.activitytest.city;

import com.bigkoo.pickerview.model.IPickerViewData;

/**
 * Created by zhangtao on 16/11/5.
 */
public class DistrictModel implements IPickerViewData
{
    private String name;
    private String zipcode;

    public DistrictModel()
    {
        super();
    }

    public String getZipcode()
    {
        return zipcode;
    }

    public void setZipcode(String zipcode)
    {
        this.zipcode = zipcode;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return name;
    }

    @Override
    public String getPickerViewText()
    {
        return name;
    }
}
